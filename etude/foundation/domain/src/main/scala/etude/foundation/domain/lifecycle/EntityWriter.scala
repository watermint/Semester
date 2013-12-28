package etude.foundation.domain.lifecycle

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}

trait EntityWriter[ID <: Identity[_], E <: Entity[ID], M[+A]] extends EntityIO {
  type This <: EntityWriter[ID, E, M]

  def store(entity: E)(implicit context: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  def deleteByIdentity(identity: ID)(implicit context: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  def delete(entity: E)(implicit context: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]] = deleteByIdentity(entity.identity)
}
