package etude.pintxos.chatwork.domain.event.message

import etude.domain.core.event.mutable.IdentityEventPublisherSupport
import etude.domain.core.event.{IdentityEvent, IdentityEventType}
import etude.domain.core.lifecycle.EntityIOContext
import etude.gazpacho.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0UpdateSubscriber}
import etude.pintxos.chatwork.domain.lifecycle.room.AsyncRoomRepository
import etude.pintxos.chatwork.domain.model.message.MessageId
import etude.pintxos.chatwork.domain.model.room.RoomId
import org.json4s._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

private[message]
case class AsyncMessageEventPublisherOnV0Api(context: EntityIOContext[Future])
  extends AsyncMessageEventPublisher
  with IdentityEventPublisherSupport[MessageId, Future]
  with V0UpdateSubscriber
  with V0AsyncEntityIO {

  addSubscriber(this, context)
  startUpdateScheduler(context)

  private val logger = LoggerFactory.getLogger(getClass)

  protected val subscribers: ArrayBuffer[Subscriber] = new ArrayBuffer[Subscriber]()

  val fetchMessageTimeoutMillis = 10000

  def parseMessageIds(roomId: RoomId, json: Option[JValue]): Seq[MessageId] = {
    json match {
      case Some(c) =>
        for {
          JObject(ce) <- c
          JField(messageId, JInt(flag)) <- ce
        } yield {
          MessageId(roomId, messageId.toLong)
        }
      case _ => Seq()
    }
  }

  def parseUpdateInfo(json: JValue): List[IdentityEvent[MessageId]] = {
    implicit val executionContext = getExecutionContext(context)
    (for {
      JObject(update) <- json
      JField("update_info", JObject(updateInfo)) <- update
      JField("room", JObject(roomData)) <- updateInfo
      JField(roomIdValue, JObject(roomUpdate)) <- roomData
    } yield {
      val roomDataMap = roomData.toMap
      val roomId = new RoomId(roomIdValue.toLong)

      val editedMessages: Seq[MessageId] = parseMessageIds(roomId, roomDataMap.get("ce"))
      val deletedMessages: Seq[MessageId] = parseMessageIds(roomId, roomDataMap.get("cd"))
      val latestMessage: MessageId = Await.result(
        AsyncRoomRepository.ofContext(context).latestMessage(roomId)(context),
        Duration(fetchMessageTimeoutMillis, MILLISECONDS)
      )

      editedMessages.map {
        m =>
          new IdentityEvent[MessageId](m, IdentityEventType.EntityStored)
      } ++ deletedMessages.map {
        m =>
          new IdentityEvent[MessageId](m, IdentityEventType.EntityDeleted)
      } :+
        new IdentityEvent[MessageId](latestMessage, IdentityEventType.EntityStored)
    }).flatten
  }

  def handleUpdate(json: JValue): Unit = {
    logger.debug(s"handle update: $json")
    parseUpdateInfo(json) foreach {
      ev =>
        publish(ev)(context)
    }
  }
}
