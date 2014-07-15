package etude.app.arrabbiata.ui.message.micro.session

import etude.app.arrabbiata.ui.Main
import etude.app.arrabbiata.ui.message.micro.MicroUIMessage

private[ui]
case class LoginHide() extends MicroUIMessage {
  def perform(): Unit = {
    Main.loginDialog.hide()
  }
}
