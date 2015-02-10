package semester.foundation.domain.lifecycle

import semester.foundation.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityReadableByStream[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  def resolveNext()(implicit context: EntityIOContext[M]): M[Option[E]]
}
