package etude.vino.chatwork.ui.pane

import java.net.URI

import etude.pintxos.chatwork.domain.model.room.Room
import etude.vino.chatwork.ui.UIStyles

import scalafx.scene.control.{ListCell, ListView}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

object RoomList {
  def avatarEmpty() = new Rectangle {
    width = UIStyles.avatarThumbnail
    height = UIStyles.avatarThumbnail
    fill = Color.Gray
  }
  
  def avatarUrl(uri: URI) = new ImageView {
    fitWidth = UIStyles.avatarThumbnail
    fitHeight = UIStyles.avatarThumbnail
    image = new Image(uri.toString)
  }

  val roomListView = new ListView[Room] {
    cellFactory = {
      _ =>
        new ListCell[Room] {
          item.onChange {
            (_, _, room) =>
              room match {
                case null =>
                case r =>
                  text = r.name
                  graphic = r.avatar match {
                    case None => avatarEmpty()
                    case Some(avatar) => avatarUrl(avatar)
                  }
              }
          }
        }
    }
  }
}
