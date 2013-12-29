package etude.foundation.domain.lifecycle

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}

trait EntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  def resolveNextChunk()(implicit context: EntityIOContext[M]): M[Option[EntityChunk[ID, E]]]
}
