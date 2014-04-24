package etude.domain.core.lifecycle

import scala.language.higherKinds
import etude.domain.core.model.{Entity, Identity}

trait EntityReader[ID <: Identity[_], E <: Entity[ID], M[+A]] extends EntityIO {
  def resolve(identity: ID)(implicit context: EntityIOContext[M]): M[E]

  def apply(identity: ID)(implicit context: EntityIOContext[M]): M[E] = resolve(identity)

  def containsByIdentity(identity: ID)(implicit context: EntityIOContext[M]): M[Boolean]

  def contains(entity: E)(implicit context: EntityIOContext[M]): M[Boolean] = containsByIdentity(entity.identity)
}
