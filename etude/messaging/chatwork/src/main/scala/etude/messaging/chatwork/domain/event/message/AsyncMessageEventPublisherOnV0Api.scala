package etude.messaging.chatwork.domain.event.message

import org.json4s._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.collection.mutable.ArrayBuffer
import etude.messaging.chatwork.domain.model.message.MessageId
import etude.messaging.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0UpdateSubscriber}
import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.event.mutable.IdentityEventPublisherSupport
import etude.domain.core.event.{IdentityEventType, IdentityEvent}
import etude.messaging.chatwork.domain.model.room.RoomId
import etude.messaging.chatwork.domain.lifecycle.room.AsyncRoomRepository
import org.slf4j.LoggerFactory

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
        AsyncRoomRepository.ofV0Api().latestMessage(roomId)(context),
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
