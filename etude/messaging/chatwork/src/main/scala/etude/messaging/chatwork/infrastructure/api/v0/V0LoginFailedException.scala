package etude.messaging.chatwork.infrastructure.api.v0

case class V0LoginFailedException(message: String) extends RuntimeException(message)
