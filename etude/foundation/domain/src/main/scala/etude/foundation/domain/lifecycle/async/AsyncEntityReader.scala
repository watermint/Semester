package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.EntityReader
import scala.concurrent.Future

trait AsyncEntityReader[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityReader[ID, E, Future] {

  type This <: AsyncEntityReader[ID, E]
}
