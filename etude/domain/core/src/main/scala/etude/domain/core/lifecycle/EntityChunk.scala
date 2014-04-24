package etude.domain.core.lifecycle

import etude.domain.core.model.{Identity, Entity}

case class EntityChunk[ID <: Identity[_], E <: Entity[ID]](entities: Seq[E])
