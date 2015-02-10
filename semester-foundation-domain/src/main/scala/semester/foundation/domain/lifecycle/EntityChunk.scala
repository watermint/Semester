package semester.foundation.domain.lifecycle

import semester.foundation.domain.model.{Entity, Identity}

case class EntityChunk[ID <: Identity[_], E <: Entity[ID]](entities: Seq[E])
