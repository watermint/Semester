package etude.vino.chatwork.service

import java.time.Instant

import etude.epice.logging.LoggerFactory
import etude.pintxos.chatwork.domain.service.v0.response.ChatWorkResponse
import etude.vino.chatwork.domain.Models
import etude.vino.chatwork.service.api._
import etude.vino.chatwork.service.historian.Historian
import etude.vino.chatwork.service.markasread.AutoMarkAsRead
import etude.vino.chatwork.service.recorder.Recorder
import etude.vino.chatwork.service.updater.Updater

object Service {
  val logger = LoggerFactory.getLogger(getClass)

  lazy val apiHub = Api.system.actorOf(ApiHub.props(2))
  lazy val recorder = Api.system.actorOf(Recorder.props(apiHub))
  lazy val historian = Api.system.actorOf(Historian.props(apiHub))
  lazy val updater = Api.system.actorOf(Updater.props(apiHub, 10))
  lazy val markasread = Api.system.actorOf(AutoMarkAsRead.props(apiHub))

  def startup() {
    try {
      val ver = Models.engine.client.prepareIndex()
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

    Api.system.eventStream.subscribe(apiHub, classOf[NetworkRecovered])
    Api.system.eventStream.subscribe(apiHub, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(recorder, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(historian, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(updater, classOf[NetworkRecovered])
    Api.system.eventStream.subscribe(updater, classOf[ChatWorkResponse])
    Api.system.eventStream.subscribe(markasread, classOf[ChatWorkResponse])

    Models.startup()
  }

  def shutdown(): Unit = {
    Api.system.shutdown()
    Models.engine.shutdown()
  }
}
