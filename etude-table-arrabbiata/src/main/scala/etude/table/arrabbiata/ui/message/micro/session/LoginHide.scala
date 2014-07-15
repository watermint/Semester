package etude.table.arrabbiata.ui.message.micro.session

import etude.table.arrabbiata.ui.Main
import etude.table.arrabbiata.ui.message.micro.MicroUIMessage

private[ui]
case class LoginHide() extends MicroUIMessage {
  def perform(): Unit = {
    Main.loginDialog.hide()
  }
}
