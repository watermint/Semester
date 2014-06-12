package etude.app.arrabbiata.state

import etude.messaging.chatwork.domain.model.room.{Participant, Room}

import scala.collection.mutable.ListBuffer

object Room {
  val rooms: ListBuffer[Room] = new ListBuffer[Room]

  val participants: ListBuffer[Participant] = new ListBuffer[Participant]
}
