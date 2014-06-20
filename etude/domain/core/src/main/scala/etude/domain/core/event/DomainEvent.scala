package etude.domain.core.event

import etude.domain.core.model.{Entity, Identity}

trait DomainEvent[ID <: Identity[_]] extends Entity[ID]
