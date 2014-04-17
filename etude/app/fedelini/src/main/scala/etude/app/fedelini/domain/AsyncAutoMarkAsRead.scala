package etude.app.fedelini.domain

import scala.concurrent.{future, Future}
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.foundation.domain.event.async.AsyncIdentityEventSubscriber
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.foundation.domain.event.IdentityEvent
import etude.foundation.domain.lifecycle.async.AsyncEntityIO

class AsyncAutoMarkAsRead
  extends AsyncIdentityEventSubscriber[MessageId]
  with AsyncEntityIO {

  def handleEvent(event: IdentityEvent[MessageId])(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executionContext = getExecutionContext(context)
    
    future {
      println(s"Event: MessageId[${event.identity}], Type[${event.eventType}]")
      event.identity
    }
  }
}
