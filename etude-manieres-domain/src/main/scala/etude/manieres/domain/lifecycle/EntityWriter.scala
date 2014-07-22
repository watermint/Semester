package etude.manieres.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityWriter[ID <: Identity[_], E <: Entity[ID], M[+A]] extends EntityIO {
  type This <: EntityWriter[ID, E, M]

  def store(entity: E)(implicit context: EntityIOContext[M]): M[ResultWithIdentity[This, ID, E, M]]

  def deleteByIdentity(identity: ID)(implicit context: EntityIOContext[M]): M[ResultWithIdentity[This, ID, E, M]]

  def delete(entity: E)(implicit context: EntityIOContext[M]): M[ResultWithIdentity[This, ID, E, M]] = deleteByIdentity(entity.identity)
}
