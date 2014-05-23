package etude.app.arrabbiata.ui.message.micro

import etude.app.arrabbiata.ui.Main

private[ui]
case class LoginHide() extends MicroUIMessage {
  def perform(): Unit = {
    Main.loginDialog.hide()
  }
}
