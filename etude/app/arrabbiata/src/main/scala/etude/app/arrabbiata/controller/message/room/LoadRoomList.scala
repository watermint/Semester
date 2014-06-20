package etude.app.arrabbiata.controller.message.room

import etude.app.arrabbiata.controller.message.{CallbackMessage, MessageWithSession}
import etude.app.arrabbiata.state.{Rooms, Session}
import etude.app.arrabbiata.ui.UIActor
import etude.app.arrabbiata.ui.message.UIMessage
import etude.messaging.chatwork.domain.lifecycle.room.AsyncRoomRepository

case class LoadRoomList(uiMessage: UIMessage)
  extends MessageWithSession
  with CallbackMessage {

  def perform(session: Session): Unit = {
    implicit val ioContext = session.ioContext
    implicit val execContext = session.ioContext.executor
    val roomRepo = AsyncRoomRepository.ofContext(ioContext)

    roomRepo.rooms() map {
      r =>
        Rooms.rooms.clear()
        Rooms.rooms.appendAll(r)
        UIActor.ui ! uiMessage
    }
  }
}
