package semester.service.chatwork.domain.service

case class CommandPermissionException(command: String,
                               params: Map[String, String])
  extends Exception(s"No permission for command[$command] with param[$params]")
