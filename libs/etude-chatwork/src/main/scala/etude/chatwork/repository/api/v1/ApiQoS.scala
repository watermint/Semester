package etude.chatwork.repository.api.v1

import scala.collection.mutable
import java.time.Instant

trait ApiQoS {
  protected val QOS_RETRY_DURATION_OF_FAILURE_IN_SECONDS = 3

  protected val lastLoad = mutable.HashMap[String, Instant]()

  protected def shouldFail(operation: String): Boolean = {
    lastLoad.getOrElse(operation, Instant.EPOCH)
      .plusSeconds(QOS_RETRY_DURATION_OF_FAILURE_IN_SECONDS)
      .isAfter(Instant.now)
  }
}
