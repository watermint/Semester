package etude.vino.chatwork.recorder

import java.net.URI
import java.time.ZoneOffset
import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import akka.actor.{ActorRef, Actor, Props}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.{GetUpdateRequest, LoadChatRequest}
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.{GetUpdateResponse, InitLoadResponse, LoadChatResponse}
import etude.pintxos.chatwork.domain.model.account.Account
import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.model.room.{Participant, Room}
import etude.vino.chatwork.api.{ApiEnqueue, ApiHub, PriorityNormal, PriorityRealTime}
import etude.vino.chatwork.storage.Storage
import org.json4s.JsonDSL._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

case class Recorder(apiHub: ActorRef, updateClockCycleInSeconds: Long) extends Actor {

  implicit val executionContext: ExecutionContext = ApiHub.system.dispatcher

  case class ScheduledUpdate()

  def receive: Receive = {
    case u: ScheduledUpdate =>
      apiHub ! ApiEnqueue(GetUpdateRequest(), PriorityRealTime)

    case r: InitLoadResponse =>
      r.contacts.foreach { c => update(c)}
      r.participants.foreach { p => update(p)}
      r.rooms.foreach { r => update(r)}
      ApiHub.system.scheduler.scheduleOnce(Duration.create(updateClockCycleInSeconds, TimeUnit.SECONDS), self, ScheduledUpdate())

    case r: GetUpdateResponse =>
      r.roomUpdateInfo foreach {
        room =>
          apiHub ! ApiEnqueue(LoadChatRequest(room.roomId), PriorityNormal)
      }
      ApiHub.system.scheduler.scheduleOnce(Duration.create(updateClockCycleInSeconds, TimeUnit.SECONDS), self, ScheduledUpdate())

    case r: LoadChatResponse =>
      r.chatList.foreach(update)
  }

  def update(message: Message): Unit = {
    val toAccount = message.body.to.map(_.value)
    val replies = message.body.replyTo.map(_.value)

    val json =
      ("@timestamp" -> message.ctime.toString) ~
        ("body" -> message.body.text) ~
        ("account" -> message.accountId.value) ~
        ("room" -> message.messageId.roomId.value) ~
        ("to" -> toAccount.toList) ~
        ("replyTo" -> replies)

    val indexDate = message.ctime.atOffset(ZoneOffset.UTC).toLocalDate.toString
    val indexName = s"cw-message-$indexDate"

    Storage.store(
      indexName = indexName,
      typeName = "message",
      idName = s"${message.messageId.roomId.value}-${message.messageId.messageId}",
      source = json
    )
  }

  def update(room: Room): Unit = {
    val json =
      ("roomId" -> room.roomId.value) ~
        ("roomType" -> room.roomType.name) ~
        ("name" -> room.name) ~
        ("avatarUrl" -> room.avatar.getOrElse(new URI("")).toString) ~
        ("description" -> room.description.getOrElse(""))

    Storage.store(
      indexName = "cw-room",
      typeName = "room",
      idName = s"${room.roomId.value}",
      source = json
    )
  }

  def update(account: Account): Unit = {
    val json =
      ("accountId" -> account.accountId.value) ~
        ("name" -> account.name.getOrElse("")) ~
        ("department" -> account.department.getOrElse("")) ~
        ("avatarUrl" -> account.avatarImage.getOrElse(new URI("")).toString)

    Storage.store(
      indexName = "cw-account",
      typeName = "account",
      idName = s"${account.accountId.value}",
      source = json
    )
  }

  def update(participant: Participant): Unit = {
    val json =
      ("roomId" -> participant.roomId.value) ~
        ("admin" -> participant.admin.map(_.value)) ~
        ("readonly" -> participant.readonly.map(_.value)) ~
        ("member" -> participant.member.map(_.value))

    Storage.store(
      indexName = "cw-participant",
      typeName = "participant",
      idName = s"${participant.roomId.value}",
      source = json
    )
  }
}

object Recorder {
  def props(apiHub: ActorRef, updateClockCycleInSeconds: Int): Props = Props(Recorder(apiHub, updateClockCycleInSeconds))
}