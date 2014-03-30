package etude.messaging.chatwork.domain.lifecycle.message

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.message.{Message, MessageId}

trait AsyncMessageRepository
  extends MessageRepository[Future]
  with AsyncEntityReader[MessageId, Message] {

  type This <: AsyncMessageRepository
}
