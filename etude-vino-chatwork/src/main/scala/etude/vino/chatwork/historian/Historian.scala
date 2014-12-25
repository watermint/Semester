package etude.vino.chatwork.historian

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{RoomId, Room, Participant}
import etude.vino.chatwork.stream.ChatSubscriber

import scala.concurrent.Future

case class Historian(context: EntityIOContext[Future])
  extends ChatSubscriber {

  override def update(messages: Seq[Message]): Unit = {

  }
}
