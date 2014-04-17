package etude.foundation.domain.event

import etude.foundation.domain.model.{Identity, Entity}

trait DomainEvent[ID <: Identity[_]] extends Entity[ID]
