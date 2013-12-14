package etude.chatwork.infrastructure.api.v0

case class V0LoginFailedException(message: String) extends RuntimeException(message)
