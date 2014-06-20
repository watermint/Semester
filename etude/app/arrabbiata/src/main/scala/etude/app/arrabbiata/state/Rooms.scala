package etude.app.arrabbiata.state

import etude.messaging.chatwork.domain.model.room.Room

import scala.collection.mutable.ListBuffer

object Rooms {
  val rooms: ListBuffer[Room] = new ListBuffer[Room]
}
