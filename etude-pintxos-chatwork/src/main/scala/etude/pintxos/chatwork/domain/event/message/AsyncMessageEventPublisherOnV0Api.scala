package etude.pintxos.chatwork.domain.event.message

import etude.manieres.domain.event.mutable.IdentityEventPublisherSupport
import etude.manieres.domain.event.{IdentityEvent, IdentityEventType}
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.GetUpdate
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

  def handleUpdate(json: JValue): Unit = {
    logger.debug(s"handle update: $json")
    GetUpdate.parseUpdateInfo(json)(context) foreach {
      updateInfo =>
        updateInfo.asIdentityEvent(context) foreach {
          ev =>
            publish(ev)(context)
        }
    }
  }
}
