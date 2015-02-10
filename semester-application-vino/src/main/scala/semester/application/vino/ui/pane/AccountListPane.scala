package semester.application.vino.ui.pane

import semester.service.chatwork.domain.model.account.Account
import semester.application.vino.ui.UIMessage
import semester.application.vino.ui.control.AccountListView

import scalafx.collections.ObservableBuffer

object AccountListPane {
  val accountList = new AccountListView()

  case class AccountListUpdate(accounts: Seq[Account]) extends UIMessage {
    def perform(): Unit = {
      accountList.items = ObservableBuffer(accounts)
    }
  }
}
