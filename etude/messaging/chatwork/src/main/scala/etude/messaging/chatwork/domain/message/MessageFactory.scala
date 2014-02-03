package etude.messaging.chatwork.domain.message

import scala.language.higherKinds
import etude.messaging.chatwork.domain.room.Room
import etude.foundation.domain.lifecycle.{EntityIOContext, Factory}

trait MessageFactory[M[+A]]
  extends Factory {

  def create(text: Text)(room: Room)(implicit context: EntityIOContext[M]): M[MessageId]
}
