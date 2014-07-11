package etude.adapter.chatwork.domain.infrastructure.api.v0

case class V0CommandFailureException(command: String, message: String)
  extends RuntimeException("Command[" + command + "] failed with message [" + message + "]")
