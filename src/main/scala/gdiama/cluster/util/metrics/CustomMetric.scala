package gdiama.cluster.util.metrics

import akka.actor.Address
import akka.cluster.metrics.NodeMetrics

object CustomMetric {

  final val KmsUsed = "kms-used"


  object KnsInUse {

    def unapply(nodeMetrics: NodeMetrics): Option[(Address, Int)] = {
      for {
        used <- nodeMetrics.metric(KmsUsed)
      } yield (nodeMetrics.address, used.smoothValue.toInt)
    }
  }

}
