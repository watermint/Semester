package etude.manieres.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait EntityReader[ID <: Identity[_], E <: Entity[ID], M[+A]] extends EntityIO {
  def resolve(identity: ID)(implicit context: EntityIOContext[M]): M[E]

  def apply(identity: ID)(implicit context: EntityIOContext[M]): M[E] = resolve(identity)

  def containsByIdentity(identity: ID)(implicit context: EntityIOContext[M]): M[Boolean]

  def contains(entity: E)(implicit context: EntityIOContext[M]): M[Boolean] = containsByIdentity(entity.identity)
}
