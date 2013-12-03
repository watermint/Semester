package etude.chatwork.v1

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
