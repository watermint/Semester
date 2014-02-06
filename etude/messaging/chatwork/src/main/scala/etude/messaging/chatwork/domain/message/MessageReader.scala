package etude.messaging.chatwork.domain.message

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

private[message]
trait MessageReader[M[+A]]
  extends EntityReader[MessageId, Message, M]