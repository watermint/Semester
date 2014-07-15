package etude.table.arrabbiata.controller.message

import etude.table.arrabbiata.ui.message.UIMessage

trait CallbackMessage extends Message {
  val uiMessage: UIMessage
}
