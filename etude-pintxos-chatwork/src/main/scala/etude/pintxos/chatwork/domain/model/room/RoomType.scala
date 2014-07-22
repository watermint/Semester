package etude.pintxos.chatwork.domain.model.room

import etude.manieres.domain.lifecycle.EntityNotFoundException

sealed trait RoomType {
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

case class RoomTypeUnknown() extends RoomType {
  val name: String = "unknown"
}

object RoomType {
  def isMyRoom(roomType: RoomType): Boolean = roomType.isInstanceOf[RoomTypeMy]

  def isDirectRoom(roomType: RoomType): Boolean = roomType.isInstanceOf[RoomTypeDirect]

  def isGroupRoom(roomType: RoomType): Boolean = roomType.isInstanceOf[RoomTypeGroup]

  def apply(name: String): RoomType = {
    name match {
      case "my" => RoomTypeMy()
      case "direct" => RoomTypeDirect()
      case "group" => RoomTypeGroup()
      case _ => throw EntityNotFoundException("Unknown room type: " + name)
    }
  }
}