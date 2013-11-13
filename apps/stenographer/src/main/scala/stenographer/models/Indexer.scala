package stenographer.models

import akka.actor.{Actor, Props, ActorSystem}
import etude.chatwork.RoomMeta
import stenographer.models.Indexer.IndexerTask
import org.slf4j.LoggerFactory

case class Indexer() extends Actor {
  def receive: Actor.Receive = {
    case t: IndexerTask => t.execute()
  }
}

object Indexer {
  lazy val system = ActorSystem("indexer")

  lazy val ref = system.actorOf(Props[Indexer])

  lazy val logger = LoggerFactory.getLogger(getClass)

  trait IndexerStatus
  
  case class IndexerStatusNotInitialized() extends IndexerStatus

  case class IndexerStatusLoading() extends IndexerStatus

  case class IndexerStatusLoaded() extends IndexerStatus

  case class IndexerStatusFailed(message: String) extends IndexerStatus
  
  private var indexerStatus: IndexerStatus = IndexerStatusNotInitialized()

  def currentStatus: IndexerStatus = indexerStatus

  trait IndexerTask {
    def execute(): Unit
  }

  case class InitialLoad() extends IndexerTask {
    def execute(): Unit = {
      indexerStatus = IndexerStatusLoading()
      Connect.currentSession match {
        case Some(s) =>
          Storage.putRooms(s.rooms)
          indexerStatus = IndexerStatusLoaded()
          logger.info("loaded " + Storage.loadedRooms.size + " rooms")
        case _ =>
          indexerStatus = IndexerStatusFailed("session is not available")
          logger.warn("session is not available")
      }
    }
  }

  case class LoadRoomArchive(room: RoomMeta) extends IndexerTask {
    def execute(): Unit = {

    }
  }
}