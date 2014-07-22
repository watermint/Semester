package etude.domain.core.lifecycle

case class EntityNotFoundException(message: String) extends Exception(message)