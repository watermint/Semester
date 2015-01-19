package etude.vino.chatwork.service.api

import java.io.IOException
import java.util.concurrent.TimeoutException

import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.request.{ChatWorkRequest, InitLoadRequest}
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}

import scala.concurrent.duration._

case class ApiSession() extends Actor {
  val chatworkContext = ChatWorkIOContext.fromThinConfig()

  val api = context.actorOf(Api.props(chatworkContext))

  val logger = LoggerFactory.getLogger(getClass)

  case class ContextInitialization()

  self ! ContextInitialization()

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 10 minutes) {
      case e@(_: java.net.SocketException |
              _: org.apache.http.NoHttpResponseException |
              _: IOException |
              _: TimeoutException) =>

        logger.warn("Issue on network connection", e)
        Api.ensureAvailable()
        Api.system.eventStream.publish(NetworkRecovered())
        SupervisorStrategy.Resume

      case e: Exception =>
        SupervisorStrategy.Escalate
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
  def props() = Props(ApiSession())
}