package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

case class SearchResult[E <: Entity[ID], ID <: Identity[_]] (entities: Seq[E])