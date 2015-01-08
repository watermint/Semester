package etude.vino.chatwork.api

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.{ExecutorService, Executors}

import akka.actor.SupervisorStrategy.Stop
import akka.actor._
import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.service.v0.request.ChatWorkRequest

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

case class Api() extends Actor {
  val logger = LoggerFactory.getLogger(getClass)

  val executorsPool: ExecutorService = Executors.newSingleThreadExecutor()
  implicit val executors = ExecutionContext.fromExecutorService(executorsPool)
  implicit val entityIOContext = AsyncEntityIOContextOnV0Api.fromThinConfig()

  private val executionResultTimeout = Duration(10, SECONDS)
  private val executionLock = new ReentrantLock

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy() {
      case _: Exception => Stop
    }
  }

  def receive: Receive = {
    case req: ChatWorkRequest =>
      logger.info(s"Execute request: $req on thread ${Thread.currentThread()} with context $entityIOContext")
      executionLock.lock()
      try {
        val response = Await.result(req.execute(entityIOContext), executionResultTimeout)
        sender ! response
      } finally {
        executionLock.unlock()
        logger.info(s"Finished execute request: $req on thread ${Thread.currentThread()} with context $entityIOContext")
      }
  }
}

object Api {
  val system = ActorSystem("cw-api")

  val ref = system.actorOf(Props[Api])
}
