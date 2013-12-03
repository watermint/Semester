package etude.chatwork.v0


trait Role {
  val aid: AccountId
  val roleName: String
}

case class Admin(aid: AccountId) extends Role {
  val roleName = "admin"
}

case class Member(aid: AccountId) extends Role {
  val roleName = "member"
}

case class Readonly(aid: AccountId) extends Role {
  val roleName = "readonly"
}

object Role {
  def fromRoomInfo(info: Map[String, BigInt]): List[Role] = {
    info.map(i =>
      i._2.intValue() match {
        case 1 => Admin(AccountId(i._1))
        case 2 => Member(AccountId(i._1))
        case 3 => Readonly(AccountId(i._1))
        case _ => throw UnknownChatworkProtocolException("Unknown role number: " + i._2)
      }
    ).toList
  }
}