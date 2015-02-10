package semester.application.vino.ui.control

import semester.service.chatwork.domain.model.account.Account
import semester.application.vino.domain.state.Accounts

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
