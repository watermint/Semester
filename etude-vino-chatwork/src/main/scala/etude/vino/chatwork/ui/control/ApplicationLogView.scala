package etude.vino.chatwork.ui.control

import java.time.ZoneId

import etude.vino.chatwork.service.api.ApiHistory

import scalafx.scene.control.ListCell

class ApplicationLogView extends EntityListView[ApiHistory] {
  def listCellForEntity(): ListCell[ApiHistory] = {
    new ListCell[ApiHistory] {
      item.onChange {
        (_, _, history) =>
          if (history != null) {
            val localDateTime = history.timestamp.atZone(ZoneId.systemDefault()).toLocalDateTime
            text = s"${localDateTime.toString} ${history.request}"
          }
      }
    }
  }
}
