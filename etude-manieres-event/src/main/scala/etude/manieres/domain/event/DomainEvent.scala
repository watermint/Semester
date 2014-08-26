package etude.manieres.domain.event

import etude.manieres.domain.model.{Entity, Identity}

trait DomainEvent[ID <: Identity[_]] extends Entity[ID]
