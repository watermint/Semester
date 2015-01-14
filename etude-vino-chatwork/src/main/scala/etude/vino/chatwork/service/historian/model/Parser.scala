package etude.vino.chatwork.service.historian.model

import org.json4s.JValue

trait Parser[E <: Entity] {
  def fromJSON(json: JValue): E
}
