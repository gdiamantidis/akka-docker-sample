package gdiama.cluster.util.metrics

import java.lang.System.{currentTimeMillis => newTimestamp}

import akka.actor.{ActorSystem, Address}
import akka.cluster.Cluster
import akka.cluster.metrics._
import gdiama.cluster.util.metrics.CustomMetric.KmsUsed

class KmsCapacityMetricsCollector(address: Address, decayFactor: Double) extends MetricsCollector {

  private def this(address: Address, settings: ClusterMetricsSettings) =
    this(
      address,
      EWMA.alpha(settings.CollectorMovingAverageHalfLife, settings.CollectorSampleInterval))

  def this(system: ActorSystem) = this(Cluster(system).selfAddress, ClusterMetricsExtension(system).settings)

  private val decayFactorOption = Some(decayFactor)

  def sample(): NodeMetrics = NodeMetrics(address, newTimestamp, metrics)

  def metrics(): Set[Metric] = {
    Set(kms).flatten
  }

  def kms: Option[Metric] = Metric.create(
    name = KmsUsed,
    value = 1 /*get actual value*/,
    decayFactor = None
  )

  override def close(): Unit = ()

}