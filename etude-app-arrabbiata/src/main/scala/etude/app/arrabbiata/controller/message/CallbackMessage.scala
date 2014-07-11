package etude.app.arrabbiata.controller.message

import etude.app.arrabbiata.ui.message.UIMessage

trait CallbackMessage extends Message {
  val uiMessage: UIMessage
}
