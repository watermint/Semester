package etude.vino.chatwork.ui.control

import etude.pintxos.chatwork.domain.model.account.Account
import etude.vino.chatwork.domain.state.Accounts

import scalafx.scene.control.ListCell

class AccountListView extends EntityListView[Account] {
  def listCellForEntity(): ListCell[Account] = {
    new ListCell[Account] {
      item.onChange {
        (_, _, account) =>
          account match {
            case null =>
            case a =>
              text = Accounts.nameFor(a.accountId)
              graphic = Accounts.avatar.nodeOf(a.accountId)
          }
      }
    }
  }
}
