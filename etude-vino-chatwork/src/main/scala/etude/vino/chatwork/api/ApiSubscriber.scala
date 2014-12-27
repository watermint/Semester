package etude.vino.chatwork.api

import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse

trait ApiSubscriber {
  def receive: PartialFunction[ChatWorkResponse, Unit]
}
