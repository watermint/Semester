package etude.vino.chatwork.stream

import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Participant, Room, RoomId}

import scala.collection.mutable.ArrayBuffer

case class AggregatedSubscriber() extends ChatSubscriber {

  private val subscribers: ArrayBuffer[ChatSubscriber] = new ArrayBuffer[ChatSubscriber]()

  def addSubscriber(subscriber: ChatSubscriber): Unit = {
    subscribers += subscriber
  }

  def removeSubscriber(subscriber: ChatSubscriber): Unit = {
    subscribers -= subscriber
  }

  override def update(message: Message): Unit = subscribers.foreach(_.update(message))

  override def update(roomId: RoomId): Unit = subscribers.foreach(_.update(roomId))

  override def update(room: Room): Unit = subscribers.foreach(_.update(room))

  override def update(account: Account): Unit = subscribers.foreach(_.update(account))

  override def update(participant: Participant): Unit = subscribers.foreach(_.update(participant))

  override def update(messages: Seq[Message]): Unit = subscribers.foreach(_.update(messages))
}
