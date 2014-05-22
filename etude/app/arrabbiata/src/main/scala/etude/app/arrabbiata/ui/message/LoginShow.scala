package etude.app.arrabbiata.ui.message

import etude.app.arrabbiata.ui.Main

case class LoginShow() extends UIMessage {
  def perform(): Unit = {
    Main.loginDialog.show()
  }
}
