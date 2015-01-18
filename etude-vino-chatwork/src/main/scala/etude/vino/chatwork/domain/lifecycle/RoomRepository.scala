package etude.vino.chatwork.domain.lifecycle

import java.net.URI

import etude.pintxos.chatwork.domain.model.room.{Room, RoomId, RoomType}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

case class RoomRepository(engine: ElasticSearch) extends SimpleIndexRepository[Room, RoomId] {

  val indexName: String = "cw-room"

  val typeName: String = "room"

  def toJson(entity: Room): JValue = {
    ("roomId" -> entity.roomId.value) ~
      ("roomType" -> entity.roomType.name) ~
      ("name" -> entity.name) ~
      ("avatarUrl" -> entity.avatar.getOrElse(new URI("")).toString) ~
      ("description" -> entity.description.getOrElse(""))
  }

  def fromJsonSeq(id: Option[String], source: JValue): Seq[Room] = {
    for {
      JObject(o) <- source
      JField("roomId", JInt(roomId)) <- o
      JField("roomType", JString(roomType)) <- o
      JField("name", JString(name)) <- o
      JField("avatarUrl", JString(url)) <- o
      JField("description", JString(description)) <- o
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

  def toIdentity(identity: RoomId): String = identity.value.toString()
}
