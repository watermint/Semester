package etude.table.arrabbiata.ui.message.composite.room

import etude.table.arrabbiata.ui.message.composite.CompositeUIMessage
import etude.table.arrabbiata.ui.pane.room.MergeRoomPane
import etude.pintxos.chatwork.domain.model.account.Account

case class DisplayDiffAccounts(diff: Seq[Account]) extends CompositeUIMessage {
  def perform(): Unit = {
    MergeRoomPane.diffParticipantItems.clear()
    MergeRoomPane.diffParticipantItems.appendAll(diff.distinct.sortBy(_.name))
  }
}