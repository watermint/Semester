package etude.messaging.chatwork.domain.lifecycle.message

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader
import etude.messaging.chatwork.domain.model.message.{Message, MessageId}

private[message]
trait MessageReader[M[+A]]
  extends EntityReader[MessageId, Message, M]
