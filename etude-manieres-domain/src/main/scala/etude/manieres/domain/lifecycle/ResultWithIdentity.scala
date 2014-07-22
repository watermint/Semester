package etude.manieres.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait ResultWithIdentity[+R <: EntityWriter[ID, E, M], ID <: Identity[_], E <: Entity[ID], M[+A]] {
  val result: R
  val identity: ID
}
