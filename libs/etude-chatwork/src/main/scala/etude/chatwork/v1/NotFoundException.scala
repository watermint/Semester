package etude.chatwork.v1

case class NotFoundException(message: String) extends Exception(message)