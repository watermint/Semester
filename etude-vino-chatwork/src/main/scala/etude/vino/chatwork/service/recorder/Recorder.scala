package etude.vino.chatwork.service.recorder

import java.net.URI
import java.time.ZoneOffset

import akka.actor.{Actor, ActorRef, Props}
import etude.pintxos.chatwork.domain.service.v0.request.LoadChatRequest
import etude.pintxos.chatwork.domain.service.v0.response.{LoadOldChatResponse, GetUpdateResponse, InitLoadResponse, LoadChatResponse}
import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Participant, Room}
import etude.vino.chatwork.model.converter.{MessageConverter, ParticipantConverter, AccountConverter, RoomConverter}
import etude.vino.chatwork.service.api.{ApiEnqueue, PriorityP2}
import etude.vino.chatwork.model.storage.Storage
import org.json4s.JsonDSL._

case class Recorder(apiHub: ActorRef) extends Actor {

  def receive: Receive = {
    case r: InitLoadResponse =>
      r.contacts.foreach { c => update(c)}
      r.participants.foreach { p => update(p)}
      r.rooms.foreach { r => update(r)}
      r.rooms
        .filter(_.attributes.isDefined)
        .filter(_.attributes.get.unreadCount > 0)
        .foreach {
        room =>
          apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), PriorityP2)
      }

    case r: GetUpdateResponse =>
      r.roomUpdateInfo foreach {
        room =>
          apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), PriorityP2)
      }

    case r: LoadChatResponse =>
      r.chatList.foreach(update)

    case r: LoadOldChatResponse =>
      r.messages.foreach(update)
  }

  def update(message: Message): Unit = {
    val indexDate = message.ctime.atOffset(ZoneOffset.UTC).getYear
    val indexName = s"cw-message-$indexDate"

    Storage.store(
      indexName = indexName,
      typeName = "message",
      idName = MessageConverter.toIdentity(message),
      source = MessageConverter.toJson(message)
    )
  }

  def update(room: Room): Unit = {
    Storage.store(
      indexName = "cw-room",
      typeName = "room",
      idName = RoomConverter.toIdentity(room),
      source = RoomConverter.toJson(room)
    )
  }

  def update(account: Account): Unit = {
    Storage.store(
      indexName = "cw-account",
      typeName = "account",
      idName = AccountConverter.toIdentity(account),
      source = AccountConverter.toJson(account)
    )
  }

  def update(participant: Participant): Unit = {
    Storage.store(
      indexName = "cw-participant",
      typeName = "participant",
      idName = ParticipantConverter.toIdentity(participant),
      source = ParticipantConverter.toJson(participant)
    )
  }
}

object Recorder {
  def props(apiHub: ActorRef): Props = Props(Recorder(apiHub))
}