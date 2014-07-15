package etude.pintxos.chatwork.domain.lifecycle.message

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.pintxos.chatwork.domain.infrastructure.api.{AsyncEntityIOContextOnV0Api, AsyncEntityIOContextOnV1Api}
import etude.pintxos.chatwork.domain.model.message.{Message, MessageId}

import scala.concurrent.Future

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