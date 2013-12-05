package etude.chatwork.domain

import scala.util.{Failure, Success, Try}
import etude.commons.domain.{Identity, Entity}

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
