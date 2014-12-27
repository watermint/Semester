package etude.vino.chatwork.historian

import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{ChatWorkResponse, InitLoadResponse, LoadChatResponse, LoadOldChatResponse}
import etude.pintxos.chatwork.domain.model.room.Room
import etude.vino.chatwork.api.{ApiHub, ApiSubscriber}

import scala.util.Random

case class Historian(apiHub: ApiHub)
  extends ApiSubscriber {

  apiHub.addSubscriber(this)

  def receive: PartialFunction[ChatWorkResponse, Unit] = {
    case r: InitLoadResponse =>
      Random.shuffle(r.rooms).foreach(traverse)

    case r: LoadChatResponse =>

    case r: LoadOldChatResponse =>

  }

  def traverse(room: Room): Unit = {

  }
}
