package etude.app.gare.domain

import java.util.UUID
import etude.domain.core.model.Identity

case class RailId(uuid: UUID) extends Identity[UUID] {
  override def value: UUID = uuid
}
