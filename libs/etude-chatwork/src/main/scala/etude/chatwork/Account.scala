package etude.chatwork

/**
 *
 */
case class Account(aid: BigInt,
                   gid: BigInt,
                   chatworkId: Option[String],
                   name: String,
                   avatarImagePath: String,
                   coverImagePath: String,
                   organizationName: String,
                   departmentOrDivision: String)

object Account {
  def fromAccountDat(dat: Map[String, Any]): Account = {
    Account(
      aid = dat.get("aid").get.asInstanceOf[BigInt],
      gid = dat.get("gid").get.asInstanceOf[BigInt],
      chatworkId = dat.get("cwid") match {
        case Some(cwid) =>
          if (cwid == null) {
            None
          } else {
            Some(cwid.asInstanceOf[String])
          }
        case _ => None
      },
      name = dat.get("nm").get.asInstanceOf[String],
      avatarImagePath = dat.get("av").get.asInstanceOf[String],
      coverImagePath = dat.get("cv").get.asInstanceOf[String],
      organizationName = dat.get("onm").get.asInstanceOf[String],
      departmentOrDivision = dat.get("dp").get.asInstanceOf[String]
    )
  }
}