package etude.manieres.domain.lifecycle

case class EntityNotFoundException(message: String) extends Exception(message)