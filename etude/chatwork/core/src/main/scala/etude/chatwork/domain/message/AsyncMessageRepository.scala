package etude.chatwork.domain.message

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncMessageRepository
  extends MessageRepository[Future]
  with AsyncEntityReader[MessageId, Message] {

  type This <: AsyncMessageRepository
}
