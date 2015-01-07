package etude.vino.chatwork.api

import akka.actor.{Actor, ActorSystem, Props}
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest

import scala.concurrent.Await
import scala.concurrent.duration._

case class Api() extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  implicit val executors = Api.system.dispatcher
  implicit val entityIOContext = AsyncEntityIOContextOnV0Api.fromThinConfig()

  private val executionResultTimeout = Duration(120, SECONDS)

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request: $req")
      val response = Await.result(req.execute(entityIOContext), executionResultTimeout)
      sender ! response
  }
}

object Api {
  val system = ActorSystem("cw-api")

  val ref = system.actorOf(Props[Api])
}
