package etude.foundation.domain.lifecycle.async

import etude.foundation.domain.model.{Entity, Identity}
import etude.foundation.domain.lifecycle.EntityWriter
import scala.concurrent.Future

trait AsyncEntityWriter[ID <: Identity[_], E <: Entity[ID]]
  extends AsyncEntityIO
  with EntityWriter[ID, E, Future] {

  type This <: AsyncEntityWriter[ID, E]
}
