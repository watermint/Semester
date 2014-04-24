package etude.domain.core.lifecycle

import scala.language.higherKinds
import etude.domain.core.model.{Identity, Entity}

trait EntityReadableByStream[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  def resolveNext()(implicit context: EntityIOContext[M]): M[Option[E]]
}
