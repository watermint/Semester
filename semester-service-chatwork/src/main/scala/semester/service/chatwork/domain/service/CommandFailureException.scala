package semester.service.chatwork.domain.service

case class CommandFailureException(command: String, message: String)
  extends Exception("Command[" + command + "] failed with message [" + message + "]")
