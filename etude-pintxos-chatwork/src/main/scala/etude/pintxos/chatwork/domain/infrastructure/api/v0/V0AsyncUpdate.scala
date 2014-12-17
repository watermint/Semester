package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.GetUpdate
import org.json4s._

import scala.concurrent.Future

object V0AsyncUpdate
  extends V0AsyncEntityIO {

  def update(updateLastId: Boolean = true)(implicit context: EntityIOContext[Future]): Future[JValue] = {
    GetUpdate.updateRaw(updateLastId)
  }
}
