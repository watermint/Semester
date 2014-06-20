package etude.messaging.chatwork.domain.lifecycle.message

import etude.domain.core.lifecycle.EntityReader
import etude.messaging.chatwork.domain.model.message.{Message, MessageId}

import scala.language.higherKinds

private[message]
trait MessageReader[M[+A]]
  extends EntityReader[MessageId, Message, M]
