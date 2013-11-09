package etude.chatwork

import java.time.Instant
import scala.collection.mutable

case class Stenographer(session: Session) {
  def loop(roomMeta: RoomMeta, after: Option[Instant], f: (List[Message]) => Boolean): Unit = {
    session.room(roomMeta) match {
      case None =>
      case Some(room) => {
        var currentMessages = room.messages.toList
        var oldestMessage = currentMessages.minBy(_.timestamp)
        val afterInstant = after match {
          case Some(a) => a
          case None => Instant.EPOCH
        }

        while (currentMessages.size > 0
          && f(currentMessages)
          && afterInstant.isBefore(oldestMessage.timestamp)) {

          oldestMessage = currentMessages.minBy(_.timestamp)
          currentMessages = session.messages(oldestMessage)
        }
      }
    }
  }

  def loop(roomMeta: RoomMeta, f: (List[Message]) => Boolean): Unit = {
    loop(roomMeta, None, f)
  }

  def allMessages(roomMeta: RoomMeta, after: Option[Instant]): List[Message] = {
    val allMessages = mutable.ListBuffer[Message]()

    loop(roomMeta, after, {
      (list) =>
        allMessages ++= list
        true
    })

    allMessages.toList
  }

  def allMessages(roomMeta: RoomMeta): List[Message] = allMessages(roomMeta, None)
}
