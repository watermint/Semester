package etude.foundation.domain.lifecycle

import scala.language.higherKinds
import etude.foundation.domain.model.{Entity, Identity}

trait ResultWithEntity[+R <: EntityWriter[ID, E, M], ID <: Identity[_], E <: Entity[ID], M[+A]] {
  val result: R
  val entity: E
}
