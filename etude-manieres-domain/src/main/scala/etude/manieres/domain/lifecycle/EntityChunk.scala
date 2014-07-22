package etude.manieres.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

case class EntityChunk[ID <: Identity[_], E <: Entity[ID]](entities: Seq[E])
