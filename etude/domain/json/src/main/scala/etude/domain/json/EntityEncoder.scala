package etude.domain.json

import etude.domain.core.model.{Entity, Identity}
import org.json4s.JValue

import scala.util.Try

trait EntityEncoder[ID <: Identity[_], E <: Entity[ID]] {
  def encode: JValue

  def decode(json: JValue): Try[E]
}
