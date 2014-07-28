package etude.pintxos.flickr.domain.infrastructure

class AuthPermission(permission: String) {
  val param: String = s"&perms=$permission"
}

object AuthPermission {
  val READ = new AuthPermission("read")
  val WRITE = new AuthPermission("write")
  val DELETE = new AuthPermission("delete")
}