package etude.table.arrabbiata.controller.message.room

import etude.table.arrabbiata.controller.message.{CallbackMessage, MessageWithSession}
import etude.table.arrabbiata.state.{Rooms, Session}
import etude.table.arrabbiata.ui.UIActor
import etude.table.arrabbiata.ui.message.UIMessage
import etude.table.arrabbiata.ui.message.composite.StatusUpdate
import etude.pintxos.chatwork.domain.lifecycle.room.AsyncRoomRepository

case class LoadRoomList(uiMessage: UIMessage)
  extends MessageWithSession
  with CallbackMessage {

  def perform(session: Session): Unit = {
    implicit val ioContext = session.ioContext
    implicit val execContext = session.ioContext.executor
    val roomRepo = AsyncRoomRepository.ofContext(ioContext)

    UIActor.ui ! StatusUpdate("Loading room list ...")

    roomRepo.rooms() map {
      r =>
        Rooms.rooms.set(r)
        UIActor.ui ! StatusUpdate("Finished load room list")
        UIActor.ui ! uiMessage
    }
  }
}
