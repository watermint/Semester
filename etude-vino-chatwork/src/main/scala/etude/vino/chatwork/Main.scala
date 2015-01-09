package etude.vino.chatwork

import java.time.Instant

import etude.epice.logging.LoggerFactory
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

    val apiHub = ApiHub.system.actorOf(ApiHub.props(5000))
    val recorder = ApiHub.system.actorOf(Recorder.props(apiHub))
    val historian = ApiHub.system.actorOf(Historian.props(apiHub))
    val updater = ApiHub.system.actorOf(Updater.props(apiHub, 10))
    val markasread = ApiHub.system.actorOf(MarkAsRead.props(apiHub))

    Api.system.eventStream.subscribe(apiHub, classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(recorder, classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(historian, classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(updater, classOf[ChatWorkResponse])
    ApiHub.system.eventStream.subscribe(markasread, classOf[ChatWorkResponse])

    apiHub ! ApiEnqueue(InitLoadRequest(), PriorityRealTime)
  }


  def shutdown(): Unit = {
    Api.system.shutdown()
    ApiHub.system.shutdown()
    Storage.shutdown()
  }
}
