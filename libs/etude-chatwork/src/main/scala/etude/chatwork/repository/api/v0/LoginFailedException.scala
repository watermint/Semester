package etude.chatwork.repository.api.v0

case class LoginFailedException(message: String) extends RuntimeException(message)
