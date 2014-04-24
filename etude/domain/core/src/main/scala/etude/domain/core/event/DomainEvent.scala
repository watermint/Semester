package etude.domain.core.event

import etude.domain.core.model.{Identity, Entity}

trait DomainEvent[ID <: Identity[_]] extends Entity[ID]
