package etude.vino.chatwork.stream

import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Room, Participant, RoomId}

trait ChatSubscriber {
  def update(message: Message): Unit = {}

  def update(roomId: RoomId): Unit = {}

  def update(room: Room): Unit = {}

  def update(account: Account): Unit = {}

  def update(participant: Participant): Unit = {}

  def update(messages: Seq[Message]): Unit = {}
}
