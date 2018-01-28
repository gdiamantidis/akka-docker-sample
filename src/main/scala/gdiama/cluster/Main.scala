package gdiama.cluster

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import gdiama.cluster.util.{HostIP, NodeConfig}

object Main {

  def main(args: Array[String]): Unit = {
    val nodeConfig = NodeConfig parse args
    startupOn(12000, nodeConfig)
  }

  def startupOn(webPort: Int, nodeConfig: Option[NodeConfig]): Unit = {
    nodeConfig map { c =>
      val system = ActorSystem(c.clusterName, c.config)

      system.log info s"ActorSystem ${system.name} started successfully"

      val ip = HostIP.load getOrElse "127.0.0.1"
      WebServer.startServer(ip, webPort, ServerSettings(system), system)
    }


    //    val config = ConfigFactory.parseString(
    //      s"""
    //        akka.remote.netty.tcp.port=$clusterPort
    //        akka.remote.artery.canonical.port=$clusterPort
    //      """
    //    ).withFallback(ConfigFactory.load("application-cluster.conf"))
    //
    //    val system = ActorSystem("ClusterSystem", config)
    //    WebServer.startServer("localhost", webPort, ServerSettings(system), system)

  }

}
