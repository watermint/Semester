package etude.chatwork.infrastructure.api

trait AuthContext

trait TokenAuthentication extends AuthContext {
  val token: String
}

trait PasswordAuthentication extends AuthContext {
  val organizationId: Option[String]
  val user: String
  val password: String
}

trait MixedAuthentication extends TokenAuthentication with PasswordAuthentication
