package etude.chatwork.domain.message

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityIOContext

trait MessageRepository[M[+A]]
  extends MessageReader[M] {

  def markAsRead(message: MessageId)(implicit context: EntityIOContext[M]): M[MessageId]
}
