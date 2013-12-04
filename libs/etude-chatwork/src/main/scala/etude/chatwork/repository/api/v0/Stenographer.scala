package etude.chatwork.repository.api.v0

import org.slf4j.LoggerFactory
import etude.qos.Retry.retry

case class Stenographer(session: Session) {
  lazy val logger = LoggerFactory.getLogger(getClass)

  def loop(roomId: RoomId,
           begin: Option[MessageId],
           end: Option[MessageId],
           f: (List[Message]) => Boolean,
           maxRetries: Int = 5): Unit = {
    session.room(roomId) match {
      case None =>
      case Some(room) => {
        var currentMessages = room.messages.toList
        var oldestMessage = currentMessages.minBy(_.timestamp)
        var newestMessage = currentMessages.maxBy(_.timestamp)

        val beginId: MessageId = begin.getOrElse(MessageId.EPOCH)
        val endId: MessageId = end.getOrElse(newestMessage.messageId)

        while (currentMessages.size > 0
          && f(currentMessages)
          && beginId.id < oldestMessage.messageId.id
          && endId.id >= newestMessage.messageId.id) {

          oldestMessage = currentMessages.minBy(_.timestamp)
          newestMessage = currentMessages.maxBy(_.timestamp)

          currentMessages = retry(maxRetries)(session.messages(oldestMessage))
        }
      }
    }
  }
}
