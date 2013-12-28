package etude.chatwork.domain.message

import scala.language.higherKinds

trait MessageRepository[M[+A]]
  extends MessageReader[M]
