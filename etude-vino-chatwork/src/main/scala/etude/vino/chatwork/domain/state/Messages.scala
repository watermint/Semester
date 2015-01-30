package etude.vino.chatwork.domain.state

import akka.actor.{Actor, Props}
import etude.pintxos.chatwork.domain.service.v0.response.LoadChatResponse
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.domain.lifecycle.SearchOptions
import etude.vino.chatwork.service.api.ApiSession
import etude.vino.chatwork.ui.UI
import etude.vino.chatwork.ui.pane.MessageListPane.{UpdateTimeline, UpdateToMeMessages}
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.sort.{SortBuilders, SortOrder}

import scala.concurrent.Future

class Messages extends Actor {
  implicit val executor = UI.system.dispatcher

  def updateToMeMessages(): Unit = {
    Future {
      val myId = ApiSession.myIdOption.getOrElse("")
      val response = Models.messageRepository.search(
        query = QueryBuilders.boolQuery()
          .should(QueryBuilders.matchQuery("to", myId))
          .should(QueryBuilders.matchQuery("replyTo", myId)),
        options = SearchOptions(
          sort = Some(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC)),
          size = Some(50)
        )
      )
      UI.ref ! UpdateToMeMessages(response.entities)
    }
  }

  def updateTimeline(): Unit = {
    Future {
      val response = Models.messageRepository.search(
        query = QueryBuilders.matchAllQuery(),
        options = SearchOptions(
          sort = Some(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC)),
          size = Some(100)
        )
      )
      UI.ref ! UpdateTimeline(response.entities)
    }
  }


  def receive: Receive = {
    case r: LoadChatResponse =>
      updateToMeMessages()
      updateTimeline()
  }
}

object Messages {
  val actorRef = UI.system.actorOf(Props[Messages])
}