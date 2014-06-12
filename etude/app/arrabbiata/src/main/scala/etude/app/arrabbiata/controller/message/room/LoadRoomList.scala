package etude.app.arrabbiata.controller.message.room

import etude.app.arrabbiata.controller.message.{CallbackMessage, MessageWithSession}
import etude.app.arrabbiata.state.Session
import etude.app.arrabbiata.ui.message.UIMessage

case class LoadRoomList(uiMessage: UIMessage)
  extends MessageWithSession
  with CallbackMessage {

  def perform(session: Session): Unit = {

  }
}
