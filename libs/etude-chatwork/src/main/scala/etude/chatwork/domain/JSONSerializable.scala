package etude.chatwork.domain

import scala.pickling._
import scala.pickling.json._

trait JSONSerializable {
  def toJSON: String = this.pickle.value

  override def toString: String = toJSON
}
