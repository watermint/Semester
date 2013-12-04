package etude.chatwork.domain

case class EntityNotFoundException(message: String) extends Exception(message)