package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.UpdateInfoResult

trait V0UpdateSubscriber {
  def handleUpdate(updateInfo: UpdateInfoResult): Unit
}