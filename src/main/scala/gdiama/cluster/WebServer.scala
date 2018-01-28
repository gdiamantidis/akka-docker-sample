package gdiama.cluster

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.cluster.routing.{ClusterRouterPool, ClusterRouterPoolSettings}
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, HttpApp, Route}
import akka.routing.ScatterGatherFirstCompletedPool
import akka.util.Timeout

import scala.util.{Failure, Success}


object WebServer extends HttpApp with Directives with JsonSupport {

  import akka.pattern._

  import scala.concurrent.duration._

  implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()
  implicit val timeout = Timeout(15 seconds)
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  lazy val system: ActorSystem = systemReference.get()
  lazy val router: ActorRef = system.actorOf(
    ClusterRouterPool(
      new ScatterGatherFirstCompletedPool(6, 5 seconds),
      new ClusterRouterPoolSettings(10, 1, true, None)
    ).props(TestActor.props()), "sampleScatterGather-" + UUID.randomUUID().toString)

  override def routes: Route =
    path("hello") {
      get {
        val fut = (router ? Protocol.Request(UUID.randomUUID().toString)).mapTo[Protocol.Response].map { response =>
          complete(ToResponseMarshallable(response))
        }

        onComplete(fut) {
          case Success(f) => f
          case Failure(exception) => complete(StatusCodes.InternalServerError)
        }
      }
    }
}

