package etude.vino.chatwork.model.converter

import etude.manieres.domain.model.Entity
import org.json4s.JValue
import org.json4s.native.JsonMethods._

trait Converter {
  type E <: Entity[_]

  def fromJsonSeq(json: String): Seq[E] = fromJsonSeq(parse(json))

  def fromJsonSeq(json: JValue): Seq[E]

  def fromJson(json: String): E = fromJson(parse(json))

  def fromJson(json: JValue): E = fromJsonSeq(json).last

  def toJson(entity: E): JValue

  def toJsonString(entity: E): String = compact(render(toJson(entity)))

  def toIdentity(entity: E): String
}
