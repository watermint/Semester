package etude.app.arrabbiata.ui.control

import javafx.scene.control.{ListCell => JListCell, ListView => JListView}
import javafx.scene.image.{Image => JImage, ImageView => JImageView}
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Callback

import etude.app.arrabbiata.ui.UIUnit
import etude.adapter.chatwork.domain.model.account.Account

class AccountListView extends DomainListView[Account] {
  class AccountListCell extends JListCell[Account] {
    override def updateItem(item: Account, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      item match {
        case null =>
        case i =>
          setText(i.name.getOrElse(""))
          i.avatarImage match {
            case Some(uri) =>
              // Requires JavaFX classes not ScalaFX
              setGraphic(
                new JImageView(
                  new JImage(uri.toString,
                    UIUnit.avatarThumbnail,
                    UIUnit.avatarThumbnail,
                    true, // preserve ratio
                    true, // smooth
                    true) // background loading
                )
              )
            case _ =>
              setGraphic(
                new Rectangle(
                  UIUnit.avatarThumbnail,
                  UIUnit.avatarThumbnail,
                  Color.GRAY)
              )
          }
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
