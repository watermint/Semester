package etude.messaging.chatwork.infrastructure.api

import scala.collection.mutable
import java.time.Instant
import etude.foundation.utility.qos.Throttle

trait ApiQoS {
  protected val QOS_RETRY_DURATION_OF_FAILURE_IN_SECONDS = 3

  protected val lastLoad = mutable.HashMap[String, Instant]()

  protected def shouldFail(operation: String): Boolean = {
    lastLoad.getOrElse(operation, Instant.EPOCH)
      .plusSeconds(QOS_RETRY_DURATION_OF_FAILURE_IN_SECONDS)
      .isAfter(Instant.now)
  }
}

object ApiQoS {
  lazy val throttle: Throttle = Throttle(maxQueryPerSecond = 0.5, randomWaitRangeSeconds = 4)
}
