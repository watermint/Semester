package etude.adapter.chatwork.domain.lifecycle.account

import scala.language.higherKinds

trait AccountRepository[M[+A]]
  extends AccountReader[M]