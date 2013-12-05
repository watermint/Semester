package punchedtape.punch

import punchedtape.Punch
import etude.chatwork._
import scala.io.Source
import scala.pickling._
import scala.pickling.json._
import etude.file.File
import etude.file.Dir
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import etude.commons.utility.qos.Retry
import Retry.RetryException
import java.nio.file.FileSystems
import etude.chatwork.v0._
import Retry.RetryException
import punchedtape.punch.ArchiveMessage
import scala.Some
import etude.file.File
import etude.file.Dir
import punchedtape.punch.ArchiveRoom
import Stenographer
import Retry.RetryException
import punchedtape.punch.ArchiveMessage
import etude.chatwork.v0.RoomMeta
import scala.Some
import etude.file.File
import etude.file.Dir
import punchedtape.punch.ArchiveRoom
import etude.chatwork.v0.Stenographer
import Retry.RetryException
import punchedtape.punch.ArchiveMessage
import etude.chatwork.v0.RoomMeta
import scala.Some
import etude.file.File
import etude.file.Dir
import punchedtape.punch.ArchiveRoom
import etude.chatwork.v0.Stenographer
import Retry.RetryException
import punchedtape.punch.ArchiveMessage
import etude.chatwork.v0.RoomMeta
import scala.Some
import etude.file.File
import etude.file.Dir
import punchedtape.punch.ArchiveRoom
import etude.chatwork.v0.Stenographer
import Retry.RetryException
import punchedtape.punch.ArchiveMessage
import etude.chatwork.v0.RoomMeta
import scala.Some
import etude.file.File
import etude.file.Dir
import punchedtape.punch.ArchiveRoom
import etude.chatwork.v0.Stenographer
import Retry.RetryException
import punchedtape.punch.ArchiveMessage
import etude.chatwork.v0.RoomMeta
import scala.Some
import etude.file.File
import etude.file.Dir
import punchedtape.punch.ArchiveRoom
import etude.chatwork.v0.Stenographer
import etude.commons.utility.qos.Retry

case class Archive(destDir: String) extends Punch {
  lazy val archiveDir = Dir(FileSystems.getDefault.getPath(destDir))

  lazy val logger = LoggerFactory.getLogger(getClass)

  def archiveRoom(room: RoomMeta): ArchiveRoom = {
    val filePath: File = archiveDir.resolveFile(room.roomId.toString + ".json")
    if (filePath.exists) {
      Source.fromFile(filePath.javaFile).getLines().toList.mkString.unpickle[ArchiveRoom]
    } else {
      ArchiveRoom(
        roomId = room.roomId,
        description = room.description.getOrElse("")
      )
    }
  }

  def archiveMessage(message: model.Message)(session: Session): ArchiveMessage = {
    session.account(message.aid) match {
      case Some(account) => ArchiveMessage(
        account.aid.accountId,
        account.gid.groupId,
        account.name,
        message.messageId.messageId,
        message.message,
        message.timestamp.toString
      )
      case _ =>
        ArchiveMessage(
          message.aid.accountId,
          GroupId.EMPTY.groupId,
          "",
          message.messageId.messageId,
          message.message,
          message.timestamp.toString
        )
    }
  }

  def archive(info: ArchiveRoom)(session: Session): Unit = {
    val roomDir: Dir = archiveDir.resolveDir(info.roomId.roomId)

    if (roomDir.exists) {
      logger.info("Skip room: " + info.description)
      return
    }

    val roomFilePath: File = archiveDir.resolveFile(info.roomId.roomId + ".json")
    val archiveMessages: (List[model.Message]) => Boolean = {
      (messages) =>
        messages.foreach {
          m =>
            val messageFile = roomDir.resolveFile(m.messageId.messageId + ".json")
            val am = archiveMessage(m)(session)
            FileUtils.writeStringToFile(messageFile.javaFile, am.pickle.value)
        }
        val currentLwm: List[BigInt] = info.lowWaterMark match {
          case Some(l) => List(l.id)
          case _ => List()
        }
        val currentHwm: List[BigInt] = info.highWaterMark match {
          case Some(h) => List(h.id)
          case _ => List()
        }

        val messageIds: List[BigInt] = messages.map(_.messageId.id)

        val newInfo = info.copy(
          lowWaterMark = Some(MessageId((messageIds ++ currentLwm).minBy(identity))),
          highWaterMark = Some(MessageId((messageIds ++ currentHwm).maxBy(identity)))
        )

        FileUtils.writeStringToFile(roomFilePath.javaFile, newInfo.pickle.value)

        true
    }

    if (!roomDir.exists) {
      roomDir.mkdirs
    }

    val stenographer = Stenographer(session)

    logger.info("Archiving room: " + info.description)

    stenographer.loop(info.roomId, info.highWaterMark, None, archiveMessages)
  }

  def execute(session: Session): Boolean = {
    session.rooms.filter(_.description.getOrElse("").length > 0).foreach {
      r =>
        try {
          archive(archiveRoom(r))(session)
        } catch {
          case e: RetryException =>
            e.causes.foreach {
              case c: UnknownChatworkProtocolException => logger.warn(c.getMessage + "\n" + c.payload.getOrElse("<EMPTY PAYLOAD>"), c)
              case c: Exception => logger.warn(c.getMessage, c)
            }
          case e: Exception =>
            logger.warn(e.getMessage, e)
        }
    }
    true
  }
}

case class ArchiveRoom(roomId: model.RoomId,
                       description: String,
                       lowWaterMark: Option[model.MessageId] = None,
                       highWaterMark: Option[model.MessageId] = None)


case class ArchiveMessage(accountId: String,
                          accountGroupId: String,
                          accountName: String,
                          messageId: String,
                          message: String,
                          timestamp: String)
