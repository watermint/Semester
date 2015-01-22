package etude.pintxos.chatwork.domain.service.v0

case class CommandPermissionException(command: String,
                               params: Map[String, String])
  extends Exception(s"No permission for command[$command] with param[$params]")
