package etude.qos

import java.time.{Duration, Instant}
import java.time.temporal.ChronoUnit

/**
 *
 */
case class Throttle(maxQueryPerSecond: Double) {
  if (maxQueryPerSecond <= 0) {
    throw new IllegalArgumentException("maxQueryPerSecond should grater than zero.")
  }

  var lastExecutedOn: Option[Instant] = None

  lazy val minimumWaitMillis: Long = (1 / maxQueryPerSecond * 1000).toLong

  def execute[T](f: () => T): T = {
    lastExecutedOn match {
      case Some(l) => {
        val duration = Duration.between(l, Instant.now())
        val durationMillis = duration.getSeconds * 1000 + duration.getNano / 10000000
        if (durationMillis < minimumWaitMillis) {
          Thread.sleep(minimumWaitMillis - durationMillis)
        }
      }
      case _ =>
    }

    lastExecutedOn = Some(Instant.now())
    f()
  }

}
