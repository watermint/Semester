package etude.chatwork.infrastructure.api.v1

import etude.chatwork.domain.message.{MessageId, Text, AsyncMessageFactory}
import etude.foundation.domain.lifecycle.async.AsyncFactory
import etude.chatwork.domain.room.Room
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import org.json4s._

class V1AsyncMessageFactory
  extends AsyncMessageFactory
  with V1EntityIO[Future]
  with AsyncFactory {

  def create(text: Text)(room: Room)(implicit context: EntityIOContext[Future]): Future[MessageId] = {
    implicit val executor = getExecutionContext(context)
    val endPoint = s"/v1/rooms/${room.roomId.value}/messages"
    V1AsyncApi.post(endPoint, data = List("body" -> text.text)) map {
      json =>
        val JInt(messageId) = json \ "message_id"
        MessageId(room.identity, messageId)
    }
  }
}
