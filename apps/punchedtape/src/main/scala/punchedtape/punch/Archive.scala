package punchedtape.punch

import punchedtape.{Main, Punch}
import etude.chatwork._
import scala.io.Source
import scala.pickling._
import scala.pickling.json._
import java.time.Instant
import etude.chatwork.RoomMeta
import etude.file.File
import etude.file.Dir
import scalax.io.Resource
import org.apache.commons.io.FileUtils

case class Archive() extends Punch {
  lazy val archiveDir = Main.home.resolveDir("archive")

  def archiveRoom(room: RoomMeta): ArchiveRoom = {
    val filePath: File = archiveDir.resolveFile(room.roomId.toString() + ".json")
    if (filePath.exists) {
      Source.fromFile(filePath.javaFile).getLines().toList.mkString.unpickle[ArchiveRoom]
    } else {
      ArchiveRoom(
        roomId = room.roomId,
        description = room.description.getOrElse("")
      )
    }
  }

  def archiveMessage(message: Message)(session: Session): ArchiveMessage = {
    session.account(message.aid) match {
      case Some(account) => ArchiveMessage(
        account.aid,
        account.gid,
        account.name,
        message.messageId,
        message.message,
        message.timestamp.toString
      )
      case _ =>
        ArchiveMessage(
          message.aid,
          GroupId.EMPTY,
          "",
          message.messageId,
          message.message,
          message.timestamp.toString
        )
    }
  }

  def archive(info: ArchiveRoom)(session: Session): Unit = {
    val roomDir: Dir = archiveDir.resolveDir(info.roomId.roomId)
    val roomFilePath: File = archiveDir.resolveFile(info.roomId.roomId + ".json")
    val archiveMessages: (List[Message]) => Boolean = {
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

    println("Archiving room: " + info.description)

//    stenographer.loop(info.roomId, info.lowWaterMark, info.highWaterMark, archiveMessages)
    stenographer.loop(info.roomId, info.highWaterMark, None, archiveMessages)
  }

  def execute(session: Session): Boolean = {
    session.rooms.filter(_.description.getOrElse("").length > 0).foreach(r => archive(archiveRoom(r))(session))
    true
  }
}

case class ArchiveRoom(roomId: RoomId,
                       description: String,
                       lowWaterMark: Option[MessageId] = None,
                       highWaterMark: Option[MessageId] = None)


case class ArchiveMessage(accountId: AccountId,
                          accountGroupId: GroupId,
                          accountName: String,
                          messageId: MessageId,
                          message: String,
                          timestamp: String)
