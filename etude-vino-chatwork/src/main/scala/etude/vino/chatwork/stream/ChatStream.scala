package etude.vino.chatwork.stream

import java.util.concurrent.{Executors, ExecutorService}

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.manieres.domain.lifecycle.async.AsyncEntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.{InitLoad, LoadChat}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.UpdateInfoResult
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0UpdateSubscriber}
import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Room, Participant, RoomId}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContext, Future}

case class ChatStream(context: EntityIOContext[Future])
  extends V0AsyncEntityIO
  with V0UpdateSubscriber {

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
  }

  private val subscribers = AggregatedSubscriber()

  def addSubscriber(subscriber: ChatSubscriber): Unit = {
    subscribers.addSubscriber(subscriber)
  }

  def removeSubscriber(subscriber: ChatSubscriber): Unit = {
    subscribers.removeSubscriber(subscriber)
  }

  def handleUpdate(updateInfo: UpdateInfoResult): Unit = {
    updateInfo.roomUpdateInfo foreach {
      u =>
        implicit val executor = getExecutionContext(context)
        subscribers.update(u.roomId)
        LoadChat.loadChat(u.roomId)(context) map {
          chat =>
            chat.chatList foreach {
              message =>
                subscribers.update(message)
            }
        }
    }
  }

  def start(): Unit = {
    implicit val executor = getExecutionContext(context)

    InitLoad.initLoad()(context) map {
      load =>
        load.contacts.foreach { c => subscribers.update(c) }
        load.participants.foreach { p => subscribers.update(p) }
        load.rooms.foreach { r => subscribers.update(r) }
    }

    addSubscriber(this, context)
    startUpdateScheduler(context)
  }

  def shutdown(): Unit = {
    shutdownUpdateScheduler(context)
    removeSubscriber(this, context)
  }

}

object ChatStream {
  def fromThinConfig(): ChatStream = {
    val executorsPool: ExecutorService = Executors.newCachedThreadPool()
    implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

    ChatStream(
      AsyncEntityIOContextOnV0Api.fromThinConfig()
    )
  }
}