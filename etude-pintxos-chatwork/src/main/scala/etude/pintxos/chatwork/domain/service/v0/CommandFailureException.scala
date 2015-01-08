package etude.pintxos.chatwork.domain.service.v0

case class CommandFailureException(command: String, message: String)
  extends Exception("Command[" + command + "] failed with message [" + message + "]")
