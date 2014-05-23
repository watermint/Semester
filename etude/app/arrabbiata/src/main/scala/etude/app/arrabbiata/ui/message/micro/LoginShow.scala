package etude.app.arrabbiata.ui.message.micro

import etude.app.arrabbiata.ui.Main

private[ui]
case class LoginShow() extends MicroUIMessage {
  def perform(): Unit = {
    Main.loginDialog.show()
  }
}
