package etude.messaging.chatwork.domain.infrastructure.api.v0

import scala.language.higherKinds
import etude.messaging.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api
import scala.concurrent.{future, Future}
import etude.foundation.domain.lifecycle.async.AsyncEntityIO

class V0AsyncUpdateHandler(context: EntityIOContextOnV0Api[Future])
  extends Runnable
  with AsyncEntityIO {

  def run(): Unit = {
    implicit val executionContext = getExecutionContext(context)
    future {
      println("update...")
      V0AsyncUpdate.update(updateLastId = true)(context) map {
        json =>
          println(s"update: $json")
          context.updateSubscribers.foreach(_.handleUpdate(json))
      } recover {
        case e: Throwable => println(s"error on update: $e")
      }
    }
  }
}
