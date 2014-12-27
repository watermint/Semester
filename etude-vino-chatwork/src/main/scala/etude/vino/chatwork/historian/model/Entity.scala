package etude.vino.chatwork.historian.model

import org.json4s.JValue

trait Entity {
  def toJSON: JValue
}
