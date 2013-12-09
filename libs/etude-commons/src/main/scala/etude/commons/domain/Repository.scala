package etude.commons.domain

import scala.util.{Failure, Success, Try}

trait Repository[ID <: Identity[_], E <: Entity[ID]] {
  def resolve(identifier: ID): Try[E]

  def contains(identifier: ID): Try[Boolean] = {
    resolve(identifier) match {
      case Success(s) => Success(true)
      case Failure(a: EntityNotFoundException) => Success(false)
      case Failure(f) => Failure(f)
    }
  }

  def contains(entity: E): Try[Boolean]
}
