package etude.chatwork

case class Stenographer(session: Session) {
  def loop(roomId: RoomId, begin: Option[MessageId], end: Option[MessageId], f: (List[Message]) => Boolean): Unit = {
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

          currentMessages = session.messages(oldestMessage)
        }
      }
    }
  }
}
