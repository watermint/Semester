package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.domain.core.lifecycle.async.AsyncEntityIO
import etude.foundation.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.higherKinds

class V0AsyncUpdateHandler(context: EntityIOContextOnV0Api[Future])
  extends Runnable
  with AsyncEntityIO {

  val logger = LoggerFactory.getLogger(getClass)
  val updateTimeoutInMillis = 10000

  def run(): Unit = {
    implicit val executionContext = getExecutionContext(context)
    try {
      val json = Await.result(
        V0AsyncUpdate.update(updateLastId = true)(context),
        Duration(updateTimeoutInMillis, MILLISECONDS)
      )
      context.updateSubscribers.foreach {
        s =>
          logger.debug(s"handle on $s for $json")
          s.handleUpdate(json)
      }
    } catch {
      case t: Throwable =>
        logger.error("Error on V0asyncUpdate.update", t)
    }
  }
}
