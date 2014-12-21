package etude.vino.chatwork.stream

import java.util.concurrent.{Executors, ExecutorService}

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.manieres.domain.lifecycle.async.AsyncEntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.{InitLoad, LoadChat}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.model.UpdateInfoResult
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0UpdateSubscriber}
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.RoomId

import scala.concurrent.{ExecutionContext, Future}

case class MessageStream(context: EntityIOContext[Future],
                         messageStream: (Message => Unit),
                         roomStream: (RoomId => Unit))
  extends V0AsyncEntityIO
  with V0UpdateSubscriber {

  def handleUpdate(updateInfo: UpdateInfoResult): Unit = {
    updateInfo.roomUpdateInfo foreach {
      u =>
        implicit val executor = getExecutionContext(context)
        roomStream(u.roomId)
        LoadChat.loadChat(u.roomId)(context) map {
          chat =>
            chat.chatList foreach {
              message =>
                messageStream(message)
            }
        }
    }
  }

  def start(): Unit = {
    InitLoad.initLoad()(context)

    addSubscriber(this, context)
    startUpdateScheduler(context)
  }

  def shutdown(): Unit = {
    shutdownUpdateScheduler(context)
    removeSubscriber(this, context)
  }

}

object MessageStream {
  def fromThinConfig(messageStream: (Message => Unit),
                     roomStream: (RoomId => Unit)): MessageStream = {
    val executorsPool: ExecutorService = Executors.newCachedThreadPool()
    implicit val executors = ExecutionContext.fromExecutorService(executorsPool)

    MessageStream(
      AsyncEntityIOContextOnV0Api.fromThinConfig(),
      messageStream,
      roomStream
    )
  }
}