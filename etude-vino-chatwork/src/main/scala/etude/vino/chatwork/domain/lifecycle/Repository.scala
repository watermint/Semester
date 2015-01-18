package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}
import etude.vino.chatwork.domain.infrastructure.ElasticSearch
import org.json4s.{JString, JValue}
import org.json4s.native.JsonMethods._

trait Repository[E <: Entity[ID], ID <: Identity[_]] {
  val engine: ElasticSearch

  def indexName(entity: E): String

  def typeName(entity: E): String

  def fromJsonSeq(json: String): Seq[E] = fromJsonSeq(parse(json))

  def fromJsonSeq(json: JValue): Seq[E] = {
    val id: String = (json \ "_id").asInstanceOf[JString].values
    val source: JValue = json \ "_source"

    fromJsonSeq(id, source)
  }

  def fromJsonSeq(id: String, source: JValue): Seq[E]

  def fromJson(json: String): E = fromJson(parse(json))

  def fromJson(json: JValue): E = fromJsonSeq(json).last

  def toJson(entity: E): JValue

  def toJsonString(entity: E): String = compact(render(toJson(entity)))

  def toIdentity(identity: ID): String

  def update(entity: E): Long = {
    engine.update(
      indexName(entity),
      typeName(entity),
      toIdentity(entity.identity),
      toJson(entity)
    )
  }

  def delete(entity: E): Long = {
    engine.delete(
      indexName(entity),
      typeName(entity),
      toIdentity(entity.identity)
    )
  }
}
