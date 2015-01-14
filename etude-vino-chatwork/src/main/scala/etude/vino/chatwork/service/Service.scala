package etude.vino.chatwork.service

import java.time.Instant

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.service.api._
import etude.vino.chatwork.service.historian.Historian
import etude.vino.chatwork.service.markasread.MarkAsRead
import etude.vino.chatwork.service.recorder.Recorder
import etude.vino.chatwork.service.storage.Storage
import etude.vino.chatwork.service.updater.Updater

object Service {
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
//    val historian = Api.system.actorOf(Historian.props(apiHub))
//    val updater = Api.system.actorOf(Updater.props(apiHub, 10))
//    val markasread = Api.system.actorOf(MarkAsRead.props(apiHub))

    Api.system.eventStream.subscribe(apiHub, classOf[RefreshSemaphore])
    Api.system.eventStream.subscribe(apiHub, classOf[ChatWorkResponse[_]])
    Api.system.eventStream.subscribe(recorder, classOf[ChatWorkResponse[_]])
//    Api.system.eventStream.subscribe(historian, classOf[ChatWorkResponse[_]])
//    Api.system.eventStream.subscribe(updater, classOf[ChatWorkResponse[_]])
//    Api.system.eventStream.subscribe(markasread, classOf[ChatWorkResponse[_]])
  }


  def shutdown(): Unit = {
    Api.system.shutdown()
    Storage.shutdown()
  }
}