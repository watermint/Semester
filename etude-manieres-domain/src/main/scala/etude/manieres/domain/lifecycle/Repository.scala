package etude.manieres.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

import scala.language.higherKinds

trait Repository[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends EntityReader[ID, E, M]
  with EntityWriter[ID, E, M] {

  type This <: Repository[ID, E, M]
}
