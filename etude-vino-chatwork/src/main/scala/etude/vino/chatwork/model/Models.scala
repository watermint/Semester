package etude.vino.chatwork.model

import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.model.state.{Rooms, ApplicationLog, Accounts}
import etude.vino.chatwork.service.api.{Api, ApiHistory}

object Models {
  def startup(): Unit = {
    Api.system.eventStream.subscribe(Accounts.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(Rooms.actorRef, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(ApplicationLog.actorRef, classOf[ApiHistory])
  }
}
