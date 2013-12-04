package etude.chatwork.repository

case class EntityNotFoundException(message: String) extends Exception(message)