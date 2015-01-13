package etude.vino.chatwork.api

import akka.actor._
import etude.pintxos.chatwork.domain.service.v0.request.{ChatWorkRequest, InitLoadRequest}
import etude.pintxos.chatwork.domain.service.v0.{SessionTimeoutException, NoSessionAvailableException, ChatWorkApi, ChatWorkIOContext}

case class ApiSession() extends Actor {
  val chatworkContext = ChatWorkIOContext.fromThinConfig()

  val api = Api.system.actorOf(Api.props(chatworkContext))

  case class ContextInitialization()

  self ! ContextInitialization()

  override val supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 1) {
      case _: NoSessionAvailableException =>
        SupervisorStrategy.Restart

      case _: SessionTimeoutException =>
        SupervisorStrategy.Restart

      case _: Exception =>
        SupervisorStrategy.Restart
    }
  }

  def receive = {
    case _: ContextInitialization =>
      ChatWorkApi.login(chatworkContext)
      api ! InitLoadRequest()

    case r: ChatWorkRequest =>
      api ! r
  }

}

object ApiSession {
  val system = ActorSystem("cw-apisession")

  def props() = Props(ApiSession())
}