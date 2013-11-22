package etude.chatwork

import java.time.Instant
import scala.collection.mutable

case class Stenographer(session: Session) {
  def loop(roomId: BigInt, begin: Option[BigInt], end: Option[BigInt], f: (List[Message]) => Boolean): Unit = {
    session.room(roomId) match {
      case None =>
      case Some(room) => {
        var currentMessages = room.messages.toList
        var oldestMessage = currentMessages.minBy(_.timestamp)
        var newestMessage = currentMessages.maxBy(_.timestamp)

        val beginId: BigInt = begin.getOrElse(0)
        val endId: BigInt = end.getOrElse(newestMessage.messageId)

        while (currentMessages.size > 0
          && f(currentMessages)
          && beginId < oldestMessage.messageId
          && endId >= newestMessage.messageId) {

          oldestMessage = currentMessages.minBy(_.timestamp)
          newestMessage = currentMessages.maxBy(_.timestamp)

          currentMessages = session.messages(oldestMessage)
        }
      }
    }
  }
}
