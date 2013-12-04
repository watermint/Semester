package etude.chatwork.repository.api.v1

trait AuthContext

trait TokenAuthentication extends AuthContext {
  val token: String
}

trait PasswordAuthentication extends AuthContext {
  val organizationId: String
  val user: String
  val password: String
}
