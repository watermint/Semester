package etude.vino.chatwork.domain.state

import akka.actor.{Actor, Props}
import etude.pintxos.chatwork.domain.service.v0.response.LoadChatResponse
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.service.api.ApiSession
import etude.vino.chatwork.ui.UI
import etude.vino.chatwork.ui.pane.MessageListPane.UpdateToMeMessages
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.sort.{SortOrder, SortBuilders}

import scala.concurrent.Future

class Messages extends Actor {
  implicit val executor = UI.system.dispatcher

  def receive: Receive = {
    case r: LoadChatResponse =>
      Future {
        val myId = ApiSession.myIdOption.getOrElse("")
        val response = Models.messageRepository.search(
          query = QueryBuilders.boolQuery()
            .should(QueryBuilders.matchQuery("to", myId))
            .should(QueryBuilders.matchQuery("replyTo", myId)),
          sort = Some(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC))
        )
        UI.ref ! UpdateToMeMessages(response.entities)
      }
  }
}

object Messages {
  val actorRef = UI.system.actorOf(Props[Messages])
}