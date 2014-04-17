package etude.messaging.chatwork.domain.event.message

import etude.foundation.domain.event.async.{AsyncIdentityEventSubscriber, AsyncIdentityEventPublisher}
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.collection.mutable
import scala.concurrent.{Lock, Future}
import etude.messaging.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api

trait AsyncMessageEventPublisher
  extends AsyncIdentityEventPublisher[MessageId] {

  type Publisher = AsyncMessageEventPublisher

  type Subscriber = AsyncIdentityEventSubscriber[MessageId]
}

object AsyncMessageEventPublisher {
  private val publishers: mutable.HashMap[EntityIOContext[Future], AsyncMessageEventPublisher] =
    new mutable.HashMap[EntityIOContext[Future], AsyncMessageEventPublisher]()

  private val publisherMutex: Lock = new Lock

  private def withPublisher[T](f: AsyncMessageEventPublisher => T)
                              (implicit context: EntityIOContext[Future]): T = {
    publisherMutex.acquire()
    try {
      val publisher = publishers.get(context) match {
        case Some(p) => p
        case _ =>
          val p: AsyncMessageEventPublisher = context match {
            case c: EntityIOContextOnV0Api[Future] =>
              new AsyncMessageEventPublisherOnV0Api(context)
            case _ =>
              throw new IllegalArgumentException("Unsupported context")
          }

          publishers.put(context, p)
          p
      }

      f(publisher)
    } finally {
      publisherMutex.release()
    }
  }

  def subscribe(subscriber: AsyncIdentityEventSubscriber[MessageId])
               (implicit context: EntityIOContext[Future]): Unit = {
    withPublisher {
      publisher =>
        publisher.subscribe(subscriber)
    }
  }

  def unsubscribe(subscriber: AsyncIdentityEventSubscriber[MessageId])
                 (implicit context: EntityIOContext[Future]): Unit = {
    withPublisher {
      publisher =>
        publisher.unsubscribe(subscriber)
    }
  }

}
