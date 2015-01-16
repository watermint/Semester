package etude.vino.chatwork.model.converter

import java.net.URI

import etude.pintxos.chatwork.domain.model.room.{Room, RoomId, RoomType}
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

object RoomConverter extends Converter {
  type E = Room

  def toJson(entity: E): JValue = {
    ("roomId" -> entity.roomId.value) ~
      ("roomType" -> entity.roomType.name) ~
      ("name" -> entity.name) ~
      ("avatarUrl" -> entity.avatar.getOrElse(new URI("")).toString) ~
      ("description" -> entity.description.getOrElse(""))
  }

  def fromJsonSeq(json: JValue): Seq[E] = {
    for {
      JObject(o) <- json
      JField("_source", JObject(source)) <- o
      JField("roomId", JInt(roomId)) <- source
      JField("roomType", JString(roomType)) <- source
      JField("name", JString(name)) <- source
      JField("avatarUrl", JString(url)) <- source
      JField("description", JString(description)) <- source
    } yield {
      new Room(
        roomId = RoomId(roomId),
        name = name,
        description = description match {
          case null | "" => None
          case d => Some(d)
        },
        attributes = None,
        roomType = RoomType(roomType),
        avatar = url match {
          case null | "" => None
          case u => Some(new URI(u))
        },
        lastUpdateTime = None
      )
    }
  }

  def toIdentity(entity: E): String = entity.roomId.value.toString()
}
