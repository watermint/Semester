package etude.messaging.chatwork.domain.account

import scala.language.higherKinds

trait AccountRepository[M[+A]]
  extends AccountReader[M]