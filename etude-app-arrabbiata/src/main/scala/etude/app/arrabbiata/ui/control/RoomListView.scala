package etude.app.arrabbiata.ui.control

import javafx.scene.control.{ListCell => JListCell, ListView => JListView}
import javafx.scene.image.{Image => JImage, ImageView => JImageView}
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Callback

import etude.app.arrabbiata.ui.UIUnit
import etude.messaging.chatwork.domain.model.room.Room

class RoomListView extends DomainListView[Room] {
  class RoomListCell extends JListCell[Room] {
    override def updateItem(item: Room, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      item match {
        case null =>
        case i =>
          setText(i.name)
          i.avatar match {
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

  class RoomCallback extends Callback[JListView[Room], JListCell[Room]] {
    override def call(param: JListView[Room]): JListCell[Room] = {
      new RoomListCell()
    }
  }

  delegate.setCellFactory(new RoomCallback())
}
