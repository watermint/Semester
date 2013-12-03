package etude.chatwork.v1.api

import scala.collection.mutable
import java.time.Instant

trait ApiQoS {
  protected val lastLoad = mutable.HashMap[String, Instant]()

  protected def shouldFail(operation: String): Boolean = {
    lastLoad.getOrElse(operation, Instant.EPOCH)
      .plusSeconds(Api.QOS_RETRY_DURATION_OF_FAILURE)
      .isAfter(Instant.now)
  }
}
