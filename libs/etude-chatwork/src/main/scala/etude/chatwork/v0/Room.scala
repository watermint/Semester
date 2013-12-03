package etude.chatwork.v0


case class Room(roomId: RoomId,
                description: String,
                publicDescription: String,
                tasks: Seq[Task],
                messages: Seq[Message]) {

  lazy val lastChatMessage: Option[Message] = {
    if (messages.length > 0) {
      Some(messages.maxBy(_.timestamp))
    } else {
      None
    }
  }
}

object Room {
  def fromLoadChat(roomId: RoomId, result: Map[String, Any]): Room = {
    Room(
      roomId = roomId,
      description = result.get("description").get.asInstanceOf[String],
      publicDescription = result.get("public_description").get.asInstanceOf[String],
      tasks = result.get("task_dat").get match {
        case Some(r) => {
          r.asInstanceOf[Map[String, Map[String, Any]]].map {
            t =>
              Task.fromTaskDat(t._2)
          }.toList
        }
        case _ => Seq()
      },
      messages = result.get("chat_list").get.asInstanceOf[List[Map[String, Any]]].map {
        c =>
          Message.fromChatList(roomId, c)
      }
    )
  }
}