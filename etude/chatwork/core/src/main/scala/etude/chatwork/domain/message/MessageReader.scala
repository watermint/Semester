package etude.chatwork.domain.message

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

trait MessageReader[M[+A]]
  extends EntityReader[MessageId, Message, M]
