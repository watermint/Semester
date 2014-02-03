package etude.messaging.chatwork.domain.message

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncFactory
import etude.messaging.chatwork.domain.room.Room
import etude.foundation.domain.lifecycle.EntityIOContext

trait AsyncMessageFactory
  extends MessageFactory[Future]
  with AsyncFactory {

  type This <: AsyncMessageFactory
}
