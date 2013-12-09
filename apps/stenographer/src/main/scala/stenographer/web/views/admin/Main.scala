package stenographer.views.admin

import etude.bootstrap.Div
import stenographer.web.App
import scala.App

object Main {
  def adminMenu(): String =
    App.adminHtml(
      title = "Menu",
      v = Div.row(
        List(leftPane, rightPane)
      )
    )

  lazy val leftPane = <div class="col-md-3">{roomSearch}{chatListPanel}</div>

  lazy val roomSearch = <div class="input-group room-search">{roomSearchBox}</div>

  lazy val roomSearchBox = List(
    <input id="room-search-box" type="text" class="form-control" placeholder="Search Room"/>,
    <span class="input-group-addon"><span class="glyphicon glyphicon-search"></span></span>
  )

  lazy val chatListPanel = <div class="panel panel-info" id="chat-list-panel">{chatListHeader}{chatListBody}</div>

  lazy val chatListHeader = <div class="panel-heading">Chat Rooms <span class="badge" id="chat-list-badge"></span></div>

  lazy val chatListBody = <div class="list-group" id="chat-list"><div class="list-group-item"><span class="text-muted">Loading...</span></div></div>

  lazy val rightPane = <div class="col-md-9" id="chat-detail"></div>

}
