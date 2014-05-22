package etude.messaging.chatwork.domain.lifecycle.message

import scala.concurrent.Future
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.message.{Message, MessageId}
import etude.domain.core.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.infrastructure.api.{AsyncEntityIOContextOnV1Api, AsyncEntityIOContextOnV0Api}

trait AsyncMessageRepository
  extends MessageRepository[Future]
  with AsyncEntityReader[MessageId, Message] {

  type This <: AsyncMessageRepository
}

object AsyncMessageRepository {
  def ofContext(context: EntityIOContext[Future]): AsyncMessageRepository = {
    context match {
      case c: AsyncEntityIOContextOnV0Api => ofV0Api()
      case c: AsyncEntityIOContextOnV1Api => ofV1Api()
      case _ => throw new IllegalArgumentException("Unsupported EntityIOContext")
    }
  }

  private def ofV0Api(): AsyncMessageRepository =
    new AsyncMessageRepositoryOnV0Api

  private def ofV1Api(): AsyncMessageRepository =
    new AsyncMessageRepositoryOnV1Api
}