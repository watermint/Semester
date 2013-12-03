package etude.chatwork.v1

import scala.util.{Failure, Success, Try}
import etude.ddd.model.{Entity, Identity}

trait Repository[ID <: Identity[_], E <: Entity[_]] {
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
