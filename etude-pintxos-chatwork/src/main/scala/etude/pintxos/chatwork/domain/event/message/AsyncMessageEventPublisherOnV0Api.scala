package etude.pintxos.chatwork.domain.event.message

import etude.epice.logging.LoggerFactory
import etude.manieres.domain.event.mutable.IdentityEventPublisherSupport
import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.GetUpdateParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.GetUpdateResponse
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncEntityIO, V0UpdateSubscriber}
import etude.pintxos.chatwork.domain.model.message.MessageId
import org.json4s._

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

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

  def handleUpdate(updateInfo: GetUpdateResponse): Unit = {
    updateInfo.roomUpdateInfo foreach {
      u =>
        u.asIdentityEvent(context) foreach {
          ev =>
            publish(ev)(context)
        }
    }
  }
}
