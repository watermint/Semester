package etude.vino.chatwork

import java.time.Instant
import java.util.concurrent.{ExecutorService, Executors}

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.infrastructure.api.AsyncEntityIOContextOnV0Api
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.InitLoadRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.ChatWorkResponse
import etude.vino.chatwork.api.{PriorityRealTime, ApiEnqueue, ApiHub, PriorityNormal}
import etude.vino.chatwork.historian.Historian
import etude.vino.chatwork.markasread.MarkAsRead
import etude.vino.chatwork.recorder.Recorder
import etude.vino.chatwork.storage.Storage
import etude.vino.chatwork.updater.Updater

import scala.concurrent.ExecutionContext

object Main {
  val logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]) {
    val executorsPool: ExecutorService = Executors.newCachedThreadPool()
    implicit val executors = ExecutionContext.fromExecutorService(executorsPool)
    implicit val context = AsyncEntityIOContextOnV0Api.fromThinConfig()

    try {
      val ver = Storage.client.prepareIndex()
        .setIndex("cw-vino")
        .setType("main")
        .setId("main")
        .setSource(s"""{"@timestamp":"${Instant.now.toString}"}""")
        .get()
        .getVersion

      logger.info(s"etude-vino-chatwork: launch data version: $ver")
    } catch {
      case _: Exception => // ignore
    }

    val apiHub = ApiHub.system.actorOf(ApiHub.props(context))

    ApiHub.system.eventStream.subscribe(ApiHub.system.actorOf(Recorder.props(apiHub)), classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(ApiHub.system.actorOf(Historian.props(apiHub)), classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(ApiHub.system.actorOf(Updater.props(apiHub, 10)), classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(ApiHub.system.actorOf(MarkAsRead.props(apiHub)), classOf[ChatWorkResponse])

    apiHub ! ApiEnqueue(InitLoadRequest(), PriorityRealTime)
  }

}
