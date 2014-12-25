package etude.vino.chatwork.stream

import etude.manieres.domain.lifecycle.EntityIOContext

import scala.concurrent.Future

case class ChatHistorian(hub: ChatSubscriber, context: EntityIOContext[Future]) {

}
