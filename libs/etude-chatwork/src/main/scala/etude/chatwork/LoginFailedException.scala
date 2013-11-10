package etude.chatwork

case class LoginFailedException(message: String) extends RuntimeException(message)
