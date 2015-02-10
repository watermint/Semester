package semester.application.vino.ui.control

import java.awt.Desktop
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

import semester.service.chatwork.domain.model.message.Message
import semester.application.vino.domain.state.Accounts
import semester.application.vino.service.api.ApiSession
import semester.application.vino.ui.UIStyles

import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.control.{Label, ListCell}
import scalafx.scene.layout.{HBox, VBox}

class MessageListView extends EntityListView[Message] {
  val displayTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  def displayTime(time: Instant): String = {
    time.atZone(ZoneId.systemDefault()).toLocalDateTime.format(displayTimeFormat)
  }

  def iconForMessage(message: Message): Node = {
    new VBox {
      children = Seq(
        new HBox {
          spacing = UIStyles.spacingWidth
          children = Seq(
            Accounts.avatar.nodeOf(message.accountId),
            new Label {
              maxWidth = UIStyles.accountNameWidth
              minWidth = UIStyles.accountNameWidth
              text = Accounts.nameFor(message.accountId)
            }
          )
        },
        new Label(displayTime(message.ctime))
      )
    }
  }

  def listCellForEntity() = {
    new ListCell[Message] {
      item.onChange {
        (_, _, message) =>
          message match {
            case null =>
            case m =>
              text = m.body.text
              wrapText = true
              graphic = iconForMessage(m)
          }
      }
    }
  }

  onMouseClicked = handle {
    val message = delegate.getSelectionModel.getSelectedItem
    ApiSession.chatWorkIOContext.get() match {
      case None =>
      case Some(context) =>
        Desktop.getDesktop.browse(message.uri(context))
    }
  }

}
