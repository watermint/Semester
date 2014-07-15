package etude.pintxos.chatwork.domain.infrastructure.api.v0

import org.json4s.JValue

trait V0UpdateSubscriber {
  def handleUpdate(json: JValue): Unit
}