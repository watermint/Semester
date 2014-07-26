package etude.pintxos.flickr.domain.model

trait Permission {
  val isPublic: Boolean
  val isFriend: Boolean
  val isFamily: Boolean

  private def toApiValue(p: Boolean): Int = {
    if (p) {
      1
    } else {
      0
    }
  }

  val mapping: Map[String, Int] = Map(
    "is_public" -> toApiValue(isPublic),
    "is_family" -> toApiValue(isFamily),
    "is_friend" -> toApiValue(isFriend)
  )
}

case class PermissionPublic() extends Permission {
  val isPublic: Boolean = true
  val isFamily: Boolean = false
  val isFriend: Boolean = false
}

case class PermissionFriendOnly() extends Permission {
  val isPublic: Boolean = false
  val isFamily: Boolean = false
  val isFriend: Boolean = true
}

case class PermissionFamilyOnly() extends Permission {
  val isPublic: Boolean = false
  val isFamily: Boolean = true
  val isFriend: Boolean = false
}

case class PermissionFriendAndFamily() extends Permission {
  val isPublic: Boolean = false
  val isFamily: Boolean = true
  val isFriend: Boolean = true
}

case class PermissionPrivate() extends Permission {
  val isPublic: Boolean = false
  val isFamily: Boolean = false
  val isFriend: Boolean = false
}
