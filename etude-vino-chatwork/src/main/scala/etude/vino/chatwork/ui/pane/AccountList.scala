package etude.vino.chatwork.ui.pane

import etude.pintxos.chatwork.domain.model.account.Account
import etude.vino.chatwork.ui.UIMessage
import etude.vino.chatwork.ui.control.AccountListView

import scalafx.collections.ObservableBuffer

object AccountList {
  val accountList = new AccountListView()

  case class AccountListUpdate(accounts: Seq[Account]) extends UIMessage {
    def perform(): Unit = {
      accountList.items = ObservableBuffer(accounts)
    }
  }
}
