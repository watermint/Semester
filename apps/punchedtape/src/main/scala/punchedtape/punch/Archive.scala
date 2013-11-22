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
        roomId = room.roomId.toString,
        description = room.description.getOrElse("")
      )
    }
  }

  def archiveMessage(message: Message)(session: Session): ArchiveMessage = {
    session.account(message.aid) match {
      case Some(account) => ArchiveMessage(
        account.aid.toString(),
        account.gid.toString(),
        account.name,
        message.messageId.toString(),
        message.message,
        message.timestamp.toString
      )
      case _ =>
        ArchiveMessage(
          message.aid.toString(),
          "",
          "",
          message.messageId.toString(),
          message.message,
          message.timestamp.toString
        )
    }
  }

  def archive(info: ArchiveRoom)(session: Session): Unit = {
    val roomDir: Dir = archiveDir.resolveDir(info.roomId)
    val roomFilePath: File = archiveDir.resolveFile(info.roomId + ".json")
    val archiveMessages: (List[Message]) => Boolean = {
      (messages) =>
        messages.foreach {
          m =>
            val messageFile = roomDir.resolveFile(m.messageId.toString() + ".json")
            val am = archiveMessage(m)(session)
            FileUtils.writeStringToFile(messageFile.javaFile, am.pickle.value)
        }
        val currentLwm: List[BigInt] = info.lowWaterMark match {
          case Some(l) => List(BigInt(l))
          case _ => List()
        }
        val currentHwm: List[BigInt] = info.highWaterMark match {
          case Some(h) => List(BigInt(h))
          case _ => List()
        }

        val messageIds: List[BigInt] = messages.map(_.messageId)

        val newInfo = info.copy(
          lowWaterMark = Some((messageIds ++ currentLwm).minBy(identity).toString()),
          highWaterMark = Some((messageIds ++ currentHwm).maxBy(identity).toString())
        )

        FileUtils.writeStringToFile(roomFilePath.javaFile, newInfo.pickle.value)

        true
    }

    if (!roomDir.exists) {
      roomDir.mkdirs
    }

    val stenographer = Stenographer(session)

    println("Archiving room: " + info.description)

    stenographer.loop(BigInt(info.roomId), info.lowWaterMarkAsBigInt, info.highWaterMarkAsBigInt, archiveMessages)
    stenographer.loop(BigInt(info.roomId), info.highWaterMarkAsBigInt, None, archiveMessages)
  }

  def execute(session: Session): Boolean = {
    session.rooms.foreach(r => archive(archiveRoom(r))(session))
    true
  }
}

case class ArchiveRoom(roomId: String,
                       description: String,
                       lowWaterMark: Option[String] = None,
                       highWaterMark: Option[String] = None) {

  lazy val lowWaterMarkAsBigInt: Option[BigInt] = lowWaterMark match {
    case Some(l) => Some(BigInt(l))
    case _ => None
  }
  lazy val highWaterMarkAsBigInt: Option[BigInt] = highWaterMark match {
    case Some(h) => Some(BigInt(h))
    case _ => None
  }
}


case class ArchiveMessage(accountId: String,
                          accountGroupId: String,
                          accountName: String,
                          messageId: String,
                          message: String,
                          timestamp: String)
