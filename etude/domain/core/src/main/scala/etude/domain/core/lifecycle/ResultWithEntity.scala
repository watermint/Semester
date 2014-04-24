package etude.domain.core.lifecycle

import scala.language.higherKinds
import etude.domain.core.model.{Entity, Identity}

trait ResultWithEntity[+R <: EntityWriter[ID, E, M], ID <: Identity[_], E <: Entity[ID], M[+A]] {
  val result: R
  val entity: E
}
