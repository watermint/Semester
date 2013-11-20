package etude.chatwork

trait Role {
  val aid: BigInt
  val roleName: String
}

case class Admin(aid: BigInt) extends Role {
  val roleName = "admin"
}

case class Member(aid: BigInt) extends Role {
  val roleName = "member"
}

case class Readonly(aid: BigInt) extends Role {
  val roleName = "readonly"
}

object Role {
  def fromRoomInfo(info: Map[String, BigInt]): List[Role] = {
    info.map(i =>
      i._2.intValue() match {
        case 1 => Admin(BigInt(i._1))
        case 2 => Member(BigInt(i._1))
        case 3 => Readonly(BigInt(i._1))
        case _ => throw UnknownChatworkProtocolException("Unknown role number: " + i._2)
      }
    ).toList
  }
}