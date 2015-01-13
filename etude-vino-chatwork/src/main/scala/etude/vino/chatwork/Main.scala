package etude.vino.chatwork

import java.time.Instant

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import etude.pintxos.chatwork.domain.service.v0.request.InitLoadRequest
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.api._
import etude.vino.chatwork.historian.Historian
import etude.vino.chatwork.markasread.MarkAsRead
import etude.vino.chatwork.recorder.Recorder
import etude.vino.chatwork.storage.Storage
import etude.vino.chatwork.updater.Updater

object Main {
  val logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]) {
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

    val apiHub = Api.system.actorOf(ApiHub.props(2))
    val recorder = Api.system.actorOf(Recorder.props(apiHub))
    val historian = Api.system.actorOf(Historian.props(apiHub))
    val updater = Api.system.actorOf(Updater.props(apiHub, 30))
    val markasread = Api.system.actorOf(MarkAsRead.props(apiHub))

    Api.system.eventStream.subscribe(apiHub, classOf[RefreshSemaphore])
    Api.system.eventStream.subscribe(apiHub, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(recorder, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(historian, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(updater, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(markasread, classOf[ChatWorkResponse])
  }


  def shutdown(): Unit = {
    Api.system.shutdown()
    Storage.shutdown()
  }
}
