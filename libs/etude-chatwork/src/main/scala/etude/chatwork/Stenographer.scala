package etude.chatwork

import etude.qos.Throttle
import scala.collection.mutable

/**
 *
 */
case class Stenographer(session: Session) {
  def allMessages(roomMeta: RoomMeta): List[Message] = {
    session.room(roomMeta) match {
      case None => List()
      case Some(room) => {
        val allMessages = mutable.ListBuffer[Message]()

        allMessages ++= room.messages

        var currentMessages = room.messages

        while (currentMessages.size > 0) {
          val lastMessage = currentMessages.minBy(_.timestamp)
          currentMessages = session.messages(lastMessage)
          allMessages ++= currentMessages
        }

        allMessages.toList
      }
    }
  }
}
