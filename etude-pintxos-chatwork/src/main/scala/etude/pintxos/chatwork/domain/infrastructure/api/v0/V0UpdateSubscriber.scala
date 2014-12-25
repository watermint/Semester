package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.UpdateInfoResponse

trait V0UpdateSubscriber {
  def handleUpdate(updateInfo: UpdateInfoResponse): Unit
}