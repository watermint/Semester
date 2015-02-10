package semester.service.chatwork.domain.service.v0.parser

import java.time.Instant

import semester.service.chatwork.domain.model.room._
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s._

object RoomParser extends ParserBase {

  def parseRooms(json: JValue): List[Room] = {
    for {
      JObject(doc) <- json
      JField("room_dat", JObject(roomDat)) <- doc
      JField(roomId, JObject(room)) <- roomDat
      JField("tp", JInt(roomType)) <- room
      JField("lt", JInt(lastUpdateTime)) <- room
    } yield {
      val r = room.toMap
      val name = r.getOrElse("n", None) match {
        case JString(n) => n
        case _ => ""
      }
      val avatar = asOptionURI(
        roomIconUrlBase,
        r.getOrElse("ic", None) match {
          case JString(a) => a
          case _ => null
        }
      )

      /* Room Attributes
       *
       * r ... read (internal sequence number)
       * c ... current chat id (internal sequence number)
       * f ... files
       * t ... tasks
       * mt ... my tasks
       * mn ... mention count
       * s ... sticky (only appears on the room is sticky. value is 1)
       */
      val readSeq: Option[BigInt] = r.get("r") match {
        case Some(JInt(seq)) => Some(seq)
        case _ => None
      }

      val currentSeq: Option[BigInt] = r.get("c") match {
        case Some(JInt(seq)) => Some(seq)
        case _ => None
      }

      val unreadCount: Int = (r.get("r"), r.get("c")) match {
        case (Some(JInt(read)), Some(JInt(current))) =>
          (current - read).toInt
        case _ =>
          0
      }

      val fileCount: Int = r.get("f") match {
        case Some(JInt(f)) => f.toInt
        case _ => 0
      }

      val taskCount: Int = r.get("t") match {
        case Some(JInt(t)) => t.toInt
        case _ => 0
      }

      val myTaskCount: Int = r.get("mt") match {
        case Some(JInt(t)) => t.toInt
        case _ => 0
      }

      val mentionCount: Int = r.get("mn") match {
        case Some(JInt(m)) => m.toInt
        case _ => 0
      }

      val sticky: Boolean = r.get("s") match {
        case Some(JInt(s)) => true
        case _ => false
      }

      new Room(
        roomId = RoomId(BigInt(roomId)),
        name = name,
        description = None,
        attributes = Some(RoomAttributes(
          sticky = sticky,
          unreadCount = unreadCount,
          mentionCount = mentionCount,
          myTaskCount = myTaskCount,
          totalTaskCount = taskCount,
          fileCount = fileCount,
          currentSequence = currentSeq,
          readSequence = readSeq
        )),
        roomType = roomType.toInt match {
          case 1 => RoomTypeGroup()
          case 2 => RoomTypeDirect()
          case 3 => RoomTypeMy()
          case _ => RoomTypeUnknown()
        },
        avatar = avatar,
        lastUpdateTime = lastUpdateTime.toLong match {
          case 0 => None
          case t => Some(Instant.ofEpochSecond(t))
        }
      )
    }
  }

}
