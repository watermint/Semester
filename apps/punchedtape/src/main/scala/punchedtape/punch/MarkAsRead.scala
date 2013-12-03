package punchedtape.punch

import scala.Boolean
import punchedtape.Punch
import org.slf4j.LoggerFactory
import etude.chatwork.v0.{Session, RoomId}

case class MarkAsRead(roomId: String) extends Punch {

  lazy val logger = LoggerFactory.getLogger(getClass)

  def execute(session: Session): Boolean = {
    session.room(RoomId(roomId)) match {
      case Some(room) =>
        logger.info("Mark as read: rid[" + roomId + "]")
        session.markAsRead(room)
        true
      case _ =>
        false
    }
  }
}
