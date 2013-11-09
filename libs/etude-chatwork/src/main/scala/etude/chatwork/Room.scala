package etude.chatwork

case class Room(roomId: BigInt,
                description: String,
                publicDescription: String,
                tasks: Seq[Task],
                messages: Seq[Message])

object Room {
  def fromLoadChat(roomId: BigInt, result: Map[String, Any]): Room = {
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