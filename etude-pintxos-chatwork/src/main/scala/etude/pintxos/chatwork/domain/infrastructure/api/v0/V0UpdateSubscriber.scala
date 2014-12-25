package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.UpdateInfoResponse

trait V0UpdateSubscriber {
  def handleUpdate(updateInfo: UpdateInfoResponse): Unit
}