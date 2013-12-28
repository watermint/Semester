package etude.foundation.domain.lifecycle

import scala.language.higherKinds
import etude.foundation.domain.model.{Identity, Entity}

trait Repository[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends EntityReader[ID, E, M]
  with EntityWriter[ID, E, M] {

  type This <: Repository[ID, E, M]
}
