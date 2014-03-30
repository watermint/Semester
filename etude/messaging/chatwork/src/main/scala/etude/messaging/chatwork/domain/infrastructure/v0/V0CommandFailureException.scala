package etude.messaging.chatwork.domain.infrastructure.v0

case class V0CommandFailureException(command: String, message: String)
  extends RuntimeException("Command[" + command + "] failed with message [" + message + "]")
