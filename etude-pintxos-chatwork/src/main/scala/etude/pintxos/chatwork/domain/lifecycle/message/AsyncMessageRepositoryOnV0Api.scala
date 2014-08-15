package etude.pintxos.chatwork.domain.lifecycle.message

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.MessageParser
import etude.pintxos.chatwork.domain.model.message._
import etude.pintxos.chatwork.domain.model.room.{Room, RoomId}

import scala.concurrent._

private[message]
class AsyncMessageRepositoryOnV0Api
  extends AsyncMessageRepository {

  type This <: AsyncMessageRepositoryOnV0Api


  def say(text: Text)(room: Room)(implicit context: EntityIOContext[Future]): Future[Option[MessageId]] = {
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._
    implicit val executor = getExecutionContext(context)

    val pdata = ("text" -> text.text) ~
      ("room_id" -> room.identity.value.toString()) ~
      ("edit_id" -> "0")

    V0AsyncApi.api(
      "send_chat",
      Map.empty,
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        None
    }
  }

  def messages(roomId: RoomId, from: MessageId, count: Int)(implicit context: EntityIOContext[Future]): Future[List[Message]] = {
    if (!from.roomId.equals(roomId)) {
      throw new IllegalArgumentException(s"Inconsistent roomId[$roomId] / messageId[${from.roomId}]")
    }
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "load_old_chat",
      Map(
        "room_id" -> from.roomId.value.toString(),
        "first_chat_id" -> from.messageId.toString()
      )
    ) map {
      json =>
        MessageParser.parseMessage(from.roomId, json).take(count)
    }
  }

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "read",
      Map(
        "room_id" -> message.roomId.value.toString(),
        "last_chat_id" -> message.messageId.toString()
      )
    ) map {
      json =>
        message
    }
  }

  def resolve(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Message] = {
    implicit val executor = getExecutionContext(context)
    messages(identity.roomId, identity, 1).map {
      m =>
        m.find(_.identity.equals(identity)).get
    }
  }

  def containsByIdentity(identity: MessageId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    messages(identity.roomId, identity, 1).map {
      m =>
        m.exists(_.identity.equals(identity))
    }
  }
}
