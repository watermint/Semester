package etude.chatwork.domain.message

import scala.language.higherKinds
import etude.chatwork.domain.room.Room
import etude.foundation.domain.lifecycle.{EntityIOContext, Factory}

trait MessageFactory[M[+A]]
  extends Factory {

  def create(text: Text)(room: Room)(implicit context: EntityIOContext[M]): M[Message]
}
