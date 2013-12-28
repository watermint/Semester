package etude.chatwork.domain.account

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.EntityReader

trait AccountReader[M[+A]]
  extends EntityReader[AccountId, Account, M]
