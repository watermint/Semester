package etude.foundation.domain.lifecycle

import etude.foundation.domain.model.{Entity, Identity}

case class Entities[ID <: Identity[_], E <: Entity[ID]](index: Int, entities: Seq[E])
