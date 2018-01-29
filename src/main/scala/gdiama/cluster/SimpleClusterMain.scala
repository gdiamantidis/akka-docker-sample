package gdiama.cluster

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory
import gdiama.cluster.util.NodeConfig

object SimpleClusterMain {

  def main(args: Array[String]): Unit = {
    startupOn(12002, None)
  }

  def startupOn(webPort: Int, nodeConfig: Option[NodeConfig]): Unit = {
    val clusterPort = 2552

    val config = ConfigFactory.parseString(
      s"""
            akka.remote.netty.tcp.port=$clusterPort
            akka.remote.artery.canonical.port=$clusterPort
          """
    ).withFallback(ConfigFactory.load("application-cluster-simple.conf"))

    val system = ActorSystem("ClusterSystem", config)
    WebServer.startServer("localhost", webPort, ServerSettings(system), system)


  }

}
