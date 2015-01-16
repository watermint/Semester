package etude.vino.chatwork.model

import akka.actor.{Props, Actor}
import etude.vino.chatwork.service.api.ApiHistory
import etude.vino.chatwork.ui.pane.ApplicationLogPane
import etude.vino.chatwork.ui.{UIMessage, UI}

import scala.collection.mutable.ArrayBuffer
import scalafx.collections.ObservableBuffer

class ApplicationLog extends Actor {
  def receive: Receive = {
    case h: ApiHistory =>
      ApplicationLog.log += h
      if (ApplicationLog.log.size > ApplicationLog.maximumSizeOfLogs) {
        ApplicationLog.log.remove(0)
      }
      UI.ref ! new UIMessage {
        def perform(): Unit = {
          ApplicationLogPane.applicationLog.items = ObservableBuffer(ApplicationLog.log)
        }
      }
  }
}

object ApplicationLog {
  val maximumSizeOfLogs = 300

  val log = new ArrayBuffer[ApiHistory]()

  val actorRef = UI.system.actorOf(Props[ApplicationLog])
}