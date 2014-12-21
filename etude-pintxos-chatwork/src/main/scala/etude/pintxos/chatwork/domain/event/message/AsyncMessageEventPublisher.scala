package etude.pintxos.chatwork.domain.event.message

import java.util.concurrent.locks.ReentrantLock

import etude.manieres.domain.event.async.{AsyncIdentityEventPublisher, AsyncIdentityEventSubscriber}
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.EntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.model.message.MessageId

import scala.collection.mutable
import scala.concurrent.Future

trait AsyncMessageEventPublisher
  extends AsyncIdentityEventPublisher[MessageId] {

  type Publisher = AsyncMessageEventPublisher

  type Subscriber = AsyncIdentityEventSubscriber[MessageId]
}

object AsyncMessageEventPublisher {
  private val publishers: mutable.HashMap[EntityIOContext[Future], AsyncMessageEventPublisher] =
    new mutable.HashMap[EntityIOContext[Future], AsyncMessageEventPublisher]()

  private val publisherMutex = new ReentrantLock

  private def withPublisher[T](f: AsyncMessageEventPublisher => T)
                              (implicit context: EntityIOContext[Future]): T = {
    publisherMutex.lock()
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
      publisherMutex.unlock()
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
