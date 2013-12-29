package etude.foundation.domain.lifecycle

import scala.language.higherKinds
import etude.foundation.domain.model.{Identity, Entity}

trait EntityReadableByStream[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  def resolveNext()(implicit context: EntityIOContext[M]): M[Option[E]]
}
