package etude.foundation.domain.lifecycle

case class EntityNotFoundException(message: String) extends Exception(message)