package etude.chatwork.domain.room

import etude.foundation.domain.lifecycle.EntityNotFoundException

trait RoomType {
  val name: String
}

case class RoomTypeMy() extends RoomType {
  val name: String = "my"
}

case class RoomTypeDirect() extends RoomType {
  val name: String = "direct"
}

case class RoomTypeGroup() extends RoomType {
  val name: String = "group"
}

object RoomType {
  def apply(name: String): RoomType = {
    name match {
      case "my" => RoomTypeMy()
      case "direct" => RoomTypeDirect()
      case "group" => RoomTypeGroup()
      case _ => throw EntityNotFoundException("Unknown room type: " + name)
    }
  }
}