package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.manieres.domain.lifecycle.async.AsyncEntityIO
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.GetUpdate
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.GetUpdateParser

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
      val updateInfoResult = Await.result(
        GetUpdate.update(updateLastId = true)(context),
        Duration(updateTimeoutInMillis, MILLISECONDS)
      )

      context.updateSubscribers.foreach {
        s =>
          s.handleUpdate(updateInfoResult)
      }
    } catch {
      case t: Throwable =>
        logger.error("Error on V0asyncUpdate.update", t)
    }
  }
}
