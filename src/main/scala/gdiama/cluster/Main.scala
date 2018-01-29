package gdiama.cluster

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import gdiama.cluster.util.{HostIP, NodeConfig}

object Main {

  def main(args: Array[String]): Unit = {
    val nodeConfig = NodeConfig parse args
  }

  def startupOn(webPort: Int, nodeConfig: Option[NodeConfig]): Unit = {
    nodeConfig map { c =>
      val system = ActorSystem(c.clusterName, c.config)

      //      ClusterRouterPool(
      //        AdaptiveLoadBalancingPool(KmsCapacityMetricsSelector),
      //        ClusterRouterPoolSettings(100, 1, true, None))

      system.log info s"ActorSystem ${system.name} started successfully"

      print(s">>> Seed nodes: ${c.config.getList("akka.cluster.seed-nodes")}")

      val ip = HostIP.load getOrElse "127.0.0.1"
      WebServer.startServer(ip, webPort, ServerSettings(system), system)
    }


  }

}
