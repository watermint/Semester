package etude.vino.chatwork.ui.state

import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.service.api.{ApiHistory, Api}

object UIState {
  def startup(): Unit = {
//    Api.system.eventStream.subscribe(Accounts.actorRef, classOf[ChatWorkResponse[_]])
//    Api.system.eventStream.subscribe(Rooms.actorRef, classOf[ChatWorkResponse[_]])
    Api.system.eventStream.subscribe(ApplicationLog.actorRef, classOf[ApiHistory])
  }
}
