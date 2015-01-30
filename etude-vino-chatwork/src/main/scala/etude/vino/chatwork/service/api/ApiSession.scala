package etude.vino.chatwork.service.api

import java.io.IOException
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.request.{ChatWorkRequest, InitLoadRequest}
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkEntityIO, ChatWorkIOContext}

case class ApiSession() extends Actor with ChatWorkEntityIO {
  val chatworkContext = ChatWorkIOContext.fromThinConfig()

  val api = context.actorOf(Api.props(chatworkContext))

  val logger = LoggerFactory.getLogger(getClass)

  self ! "login"

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy() {
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
    case "login" =>
      ChatWorkApi.login(chatworkContext)
      getMyId(chatworkContext).map(BigInt(_)) match {
        case Some(id) => ApiSession.myId.set(id)
        case None => ApiSession.myId.set(null)
      }
      ApiSession.chatWorkIOContext.set(chatworkContext)

      api ! InitLoadRequest()

    case r: ChatWorkRequest =>
      api ! r
  }

}

object ApiSession {
  def props() = Props(ApiSession())

  val myId = new AtomicReference[BigInt]()

  val chatWorkIOContext = new AtomicReference[ChatWorkIOContext]()

  def chatworkIOContextOption: Option[ChatWorkIOContext] = chatWorkIOContext.get match {
    case null => None
    case c => Some(c)
  }

  def myIdOption: Option[BigInt] = myId.get() match {
    case null => None
    case id => Some(id)
  }
}