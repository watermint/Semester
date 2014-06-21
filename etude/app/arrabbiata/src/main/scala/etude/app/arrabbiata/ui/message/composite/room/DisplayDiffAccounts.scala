package etude.app.arrabbiata.ui.message.composite.room

import etude.app.arrabbiata.ui.message.composite.CompositeUIMessage
import etude.app.arrabbiata.ui.pane.room.MergeRoomPane
import etude.messaging.chatwork.domain.model.account.Account

case class DisplayDiffAccounts(diff: Seq[Account]) extends CompositeUIMessage {
  def perform(): Unit = {
    MergeRoomPane.diffParticipantItems.clear()
    MergeRoomPane.diffParticipantItems.appendAll(diff.sortBy(_.name))
  }
}
