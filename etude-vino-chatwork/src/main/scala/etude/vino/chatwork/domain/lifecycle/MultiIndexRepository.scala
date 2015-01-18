package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

trait MultiIndexRepository[E <: Entity[ID], ID <: Identity[_]] extends Repository[E, ID] {
  val indexNamePrefix: String
}
