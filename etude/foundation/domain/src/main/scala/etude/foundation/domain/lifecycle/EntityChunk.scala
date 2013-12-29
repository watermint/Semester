package etude.foundation.domain.lifecycle

import etude.foundation.domain.model.{Identity, Entity}

case class EntityChunk[ID <: Identity[_], E <: Entity[ID]](entities: Seq[E])
