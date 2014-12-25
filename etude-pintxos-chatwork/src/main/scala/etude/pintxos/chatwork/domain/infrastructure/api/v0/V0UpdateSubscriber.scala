package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.GetUpdateResponse

trait V0UpdateSubscriber {
  def handleUpdate(updateInfo: GetUpdateResponse): Unit
}