package etude.chatwork.v0

case class LoginFailedException(message: String) extends RuntimeException(message)
