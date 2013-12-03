package etude.chatwork.v1

import scala.util.{Failure, Success, Try}

trait Repository[K <: Id[K, T], T <: Entity[K]] {
  def resolve(identifier: K): Try[T]

  def contains(identifier: K): Try[Boolean] = {
    resolve(identifier) match {
      case Success(s) => Success(true)
      case Failure(a: EntityNotFoundException) => Success(false)
      case Failure(f) => Failure(f)
    }
  }

  def contains(entity: T): Try[Boolean]
}
