package etude.vino.chatwork.service.historian.model

import org.json4s.JValue

trait Entity {
  def toJSON: JValue
}
