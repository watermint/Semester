package etude.messaging.chatwork.domain.lifecycle.message

import scala.concurrent.Future
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.message.{Message, MessageId}

trait AsyncMessageRepository
  extends MessageRepository[Future]
  with AsyncEntityReader[MessageId, Message] {

  type This <: AsyncMessageRepository
}

object AsyncMessageRepository {
  def ofV0Api(): AsyncMessageRepository =
    new AsyncMessageRepositoryOnV0Api

  def ofV1Api(): AsyncMessageRepository =
    new AsyncMessageRepositoryOnV1Api
}