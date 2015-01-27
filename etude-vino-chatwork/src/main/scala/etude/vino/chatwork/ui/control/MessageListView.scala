package etude.vino.chatwork.ui.control

import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}

import etude.pintxos.chatwork.domain.model.message.Message
import etude.vino.chatwork.domain.state.Accounts
import etude.vino.chatwork.ui.UIStyles

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
      content = Seq(
        new HBox {
          spacing = UIStyles.spacingWidth
          content = Seq(
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
}
