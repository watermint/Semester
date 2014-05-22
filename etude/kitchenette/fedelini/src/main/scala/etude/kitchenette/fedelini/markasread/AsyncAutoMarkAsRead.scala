package etude.kitchenette.fedelini.markasread

import scala.concurrent.Future
import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.event.async.AsyncIdentityEventSubscriber
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.domain.core.event.{IdentityEventType, IdentityEvent}
import etude.domain.core.lifecycle.async.AsyncEntityIO
import etude.messaging.chatwork.domain.model.room.RoomId
import etude.messaging.chatwork.domain.lifecycle.message.AsyncMessageRepository
import etude.foundation.logging.LoggerFactory

class AsyncAutoMarkAsRead(val targetRooms: Seq[RoomId] = Seq.empty)
  extends AsyncIdentityEventSubscriber[MessageId]
  with AsyncEntityIO {

  val logger = LoggerFactory.getLogger(getClass)

  def handleEvent(event: IdentityEvent[MessageId])
                 (implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executionContext = getExecutionContext(context)
    val messageRepository = AsyncMessageRepository.ofContext(context)

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

    Future.successful(event.identity)
  }
}
