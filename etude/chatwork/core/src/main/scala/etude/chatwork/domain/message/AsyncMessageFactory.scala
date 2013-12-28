package etude.chatwork.domain.message

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncFactory

trait AsyncMessageFactory
  extends MessageFactory[Future]
  with AsyncFactory {

  type This <: AsyncMessageFactory
}
