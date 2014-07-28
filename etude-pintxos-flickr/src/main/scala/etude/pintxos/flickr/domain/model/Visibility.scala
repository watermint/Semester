package etude.pintxos.flickr.domain.model

trait Visibility {
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

case class VisibilityPublic() extends Visibility {
  val isPublic: Boolean = true
  val isFamily: Boolean = false
  val isFriend: Boolean = false
}

case class VisibilityFriendOnly() extends Visibility {
  val isPublic: Boolean = false
  val isFamily: Boolean = false
  val isFriend: Boolean = true
}

case class VisibilityFamilyOnly() extends Visibility {
  val isPublic: Boolean = false
  val isFamily: Boolean = true
  val isFriend: Boolean = false
}

case class VisibilityFriendAndFamily() extends Visibility {
  val isPublic: Boolean = false
  val isFamily: Boolean = true
  val isFriend: Boolean = true
}

case class VisibilityPrivate() extends Visibility {
  val isPublic: Boolean = false
  val isFamily: Boolean = false
  val isFriend: Boolean = false
}
