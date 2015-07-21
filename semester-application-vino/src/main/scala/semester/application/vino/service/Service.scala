package semester.application.vino.service

import semester.foundation.logging.LoggerFactory
import semester.service.chatwork.domain.service.response.ChatWorkResponse
import semester.application.vino.domain.Models
import semester.application.vino.domain.lifecycle.SystemRepository
import semester.application.vino.service.api._
import semester.application.vino.service.historian.Historian
import semester.application.vino.service.markasread.AutoMarkAsRead
import semester.application.vino.service.recorder.Recorder
import semester.application.vino.service.updater.Updater

object Service {
  val logger = LoggerFactory.getLogger(getClass)

  lazy val apiHub = Api.system.actorOf(ApiHub.props(3))
  lazy val recorder = Api.system.actorOf(Recorder.props(apiHub))
  lazy val historian = Api.system.actorOf(Historian.props(apiHub))
  lazy val updater = Api.system.actorOf(Updater.props(apiHub, 30))
  lazy val markasread = Api.system.actorOf(AutoMarkAsRead.props(apiHub))

  def startup() {
    try {
      logger.info(s"vino: launch data version: ${SystemRepository.vinoVersion()}")
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
    Api.system.terminate()
    Models.engine.shutdown()
    System.exit(0)
  }
}
