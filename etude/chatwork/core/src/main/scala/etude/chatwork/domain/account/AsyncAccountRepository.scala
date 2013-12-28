package etude.chatwork.domain.account

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader

trait AsyncAccountRepository
  extends AccountRepository[Future]
  with AsyncEntityReader[AccountId, Account] {

  type This <: AsyncAccountRepository
}
