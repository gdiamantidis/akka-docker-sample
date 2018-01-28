package gdiama.cluster

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import gdiama.cluster.Protocol.Response
import spray.json.DefaultJsonProtocol

object TestActor {
  def props(): Props = Props(new TestActor)
}

object Protocol {

  final case class Response(message: String)

  final case class Request(requestId: String)

}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val responseFormat = jsonFormat1(Protocol.Response)
}

class TestActor extends Actor with ActorLogging {

  import scala.concurrent.duration._
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  @scala.throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("TestActor starting up")
  }

  override def receive: Receive = {
    case Protocol.Request(requestId) => {
      val delay = scala.util.Random.nextInt(5)
      log.info(s"Request [$requestId] received. Replying in [$delay]")
      context.system.scheduler.scheduleOnce(delay seconds, sender(), Response(s"hello from akka-http. [${self.path.name}]"))
    }

    case _ => {
      log.info("Didn't understand")
      sender() ! Response(s"I didn't understand that [${self.path.name}]")
    }
  }
}
