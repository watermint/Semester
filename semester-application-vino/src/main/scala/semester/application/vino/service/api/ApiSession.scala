package semester.application.vino.service.api

import java.io.IOException
import java.util.concurrent.TimeoutException

import akka.actor._
import semester.foundation.logging.LoggerFactory
import semester.foundation.utilities.atomic.Reference
import semester.service.chatwork.domain.service.request.{ChatWorkRequest, InitLoadRequest}
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkEntityIO, ChatWorkIOContext}

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

  val myId = Reference[BigInt]()

  val chatWorkIOContext = Reference[ChatWorkIOContext]()

  def myIdOption: Option[BigInt] = myId.get()
}