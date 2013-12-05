package etude.chatwork.model

trait RoomIcon {
  val name: String
}

case class RoomIconGroup() extends RoomIcon {
  val name: String = "group"
}

case class RoomIconCheck() extends RoomIcon {
  val name: String = "check"
}

case class RoomIconDocument() extends RoomIcon {
  val name: String = "document"
}

case class RoomIconMeeting() extends RoomIcon {
  val name: String = "meeting"
}

case class RoomIconEvent() extends RoomIcon {
  val name: String = "event"
}

case class RoomIconProject() extends RoomIcon {
  val name: String = "project"
}

case class RoomIconBusiness() extends RoomIcon {
  val name: String = "business"
}

case class RoomIconStudy() extends RoomIcon {
  val name: String = "study"
}

case class RoomIconSecurity() extends RoomIcon {
  val name: String = "security"
}

case class RoomIconStar() extends RoomIcon {
  val name: String = "star"
}

case class RoomIconIdea() extends RoomIcon {
  val name: String = "idea"
}

case class RoomIconHeart() extends RoomIcon {
  val name: String = "heart"
}

case class RoomIconMagcup() extends RoomIcon {
  val name: String = "magcup"
}

case class RoomIconBeer() extends RoomIcon {
  val name: String = "beer"
}

case class RoomIconMusic() extends RoomIcon {
  val name: String = "music"
}

case class RoomIconSports() extends RoomIcon {
  val name: String = "sports"
}

case class RoomIconTravel() extends RoomIcon {
  val name: String = "travel"
}
