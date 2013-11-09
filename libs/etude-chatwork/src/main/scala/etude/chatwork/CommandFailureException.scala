package etude.chatwork

case class CommandFailureException(command: String, message: String)
  extends RuntimeException("Command[" + command + "] failed with message [" + message + "]")
