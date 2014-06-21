package etude.app.arrabbiata.ui.control

import javafx.scene.control.{ListCell => JListCell, ListView => JListView}
import javafx.util.Callback

import etude.messaging.chatwork.domain.model.account.Account

class AccountListView extends DomainListView[Account] {
  class AccountListCell extends JListCell[Account] {
    override def updateItem(item: Account, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      item match {
        case null =>
        case i =>
          setText(i.name.getOrElse(""))
      }
    }
  }

  class AccountCallback extends Callback[JListView[Account], JListCell[Account]] {
    override def call(param: JListView[Account]): JListCell[Account] = {
      new AccountListCell()
    }
  }

  delegate.setCellFactory(new AccountCallback())

}
