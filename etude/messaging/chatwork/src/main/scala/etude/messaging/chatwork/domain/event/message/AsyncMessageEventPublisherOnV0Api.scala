package etude.messaging.chatwork.domain.event.message

import etude.foundation.domain.event.mutable.EntityEventPublisherSupport
import etude.messaging.chatwork.domain.model.message.{Message, MessageId}
import scala.concurrent.Future
import scala.collection.mutable.ArrayBuffer
import java.time.Duration
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.messaging.chatwork.domain.infrastructure.api.v0.V0UpdateSubscriber
import org.json4s.JValue

private[message]
case class AsyncMessageEventPublisherOnV0Api(context: EntityIOContext[Future])
  extends AsyncMessageEventPublisher
  with EntityEventPublisherSupport[MessageId, Message, Future]
  with V0UpdateSubscriber {

  protected val subscribers: ArrayBuffer[Subscriber] = new ArrayBuffer[Subscriber]()

  def handleUpdate(json: JValue): Unit = ???
}
