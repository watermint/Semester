package semester.foundation.domain.lifecycle

import semester.foundation.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityReadableByChunkStream[ID <: Identity[_], E <: Entity[ID], M[+A]] {
  def resolveNextChunk()(implicit context: EntityIOContext[M]): M[Option[EntityChunk[ID, E]]]
}
