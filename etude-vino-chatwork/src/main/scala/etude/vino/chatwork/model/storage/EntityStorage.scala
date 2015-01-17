package etude.vino.chatwork.model.storage

import etude.manieres.domain.model.Entity
import org.json4s.JValue
import org.json4s.native.JsonMethods._

trait EntityStorage[E <: Entity[_]] {
  def indexName(entity: E): String

  def typeName(entity: E): String

  def fromJsonSeq(json: String): Seq[E] = fromJsonSeq(parse(json))

  def fromJsonSeq(json: JValue): Seq[E]

  def fromJson(json: String): E = fromJson(parse(json))

  def fromJson(json: JValue): E = fromJsonSeq(json).last

  def toJson(entity: E): JValue

  def toJsonString(entity: E): String = compact(render(toJson(entity)))

  def toIdentity(entity: E): String

  def store(entity: E): Long = {
    Storage.store(
      indexName(entity),
      typeName(entity),
      toIdentity(entity),
      toJson(entity)
    )
  }

  def delete(entity: E): Long = {
    Storage.delete(
      indexName(entity),
      typeName(entity),
      toIdentity(entity)
    )
  }
}
