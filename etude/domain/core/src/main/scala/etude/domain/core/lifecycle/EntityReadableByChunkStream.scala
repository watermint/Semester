package etude.domain.core.lifecycle

import scala.language.higherKinds
import etude.domain.core.model.{Entity, Identity}

trait EntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  def resolveNextChunk()(implicit context: EntityIOContext[M]): M[Option[EntityChunk[ID, E]]]
}
