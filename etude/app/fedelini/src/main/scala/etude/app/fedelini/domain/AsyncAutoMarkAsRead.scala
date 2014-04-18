package etude.app.fedelini.domain

import scala.concurrent.{future, Future}
import etude.foundation.domain.lifecycle.EntityIOContext
import etude.foundation.domain.event.async.AsyncIdentityEventSubscriber
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.foundation.domain.event.{IdentityEventType, IdentityEvent}
import etude.foundation.domain.lifecycle.async.AsyncEntityIO
import etude.messaging.chatwork.domain.model.room.RoomId
import grizzled.slf4j.Logger
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository

class AsyncAutoMarkAsRead(val targetRooms: Seq[RoomId] = Seq.empty)
  extends AsyncIdentityEventSubscriber[MessageId]
  with AsyncEntityIO {

  val logger = Logger[this.type]

  def handleEvent(event: IdentityEvent[MessageId])(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executionContext = getExecutionContext(context)
    val messageRepository = AsyncMessageRepository.ofV0Api()
    
    future {
      logger.info(s"Event: MessageId[${event.identity}], Type[${event.eventType}]")
      event.eventType match {
        case IdentityEventType.EntityStored =>
          if (targetRooms.contains(event.identity.roomId)) {
            logger.info(s"Mark message [${event.identity.roomId}] as read")
            messageRepository.markAsRead(event.identity)
          }
        case IdentityEventType.EntityDeleted =>
          logger.info(s"Message deleted: ${event.identity}")
      }

      event.identity
    }
  }
}
