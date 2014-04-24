package etude.domain.core.lifecycle.async

import etude.domain.core.model.{Entity, Identity}
import etude.domain.core.lifecycle.EntityReader
import scala.concurrent.Future

trait AsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReader[ID, E, Future] {

  type This <: AsyncEntityReader[ID, E]
}
