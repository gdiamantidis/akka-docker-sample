package gdiama.cluster.util

import java.net.NetworkInterface

import scala.collection.JavaConversions._

object HostIP {

  /**
    * @return the ip adress if it's a local adress (172.16.xxx.xxx, 172.31.xxx.xxx , 192.168.xxx.xxx, 10.xxx.xxx.xxx)
    */
  def load(): Option[String] = {
    val interfaces = NetworkInterface.getNetworkInterfaces()
    val interface = interfaces find (_.getName equals "eth0")

    val ipOpt = interface flatMap { inet =>
      // the docker adress should be siteLocal
      inet.getInetAddresses find (_ isSiteLocalAddress) map (_ getHostAddress)
    }

    println(s" >>>>> HOST IP $ipOpt")

    ipOpt
  }
}