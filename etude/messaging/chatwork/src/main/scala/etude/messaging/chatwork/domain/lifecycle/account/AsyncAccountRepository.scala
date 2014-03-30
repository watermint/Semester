package etude.messaging.chatwork.domain.lifecycle.account

import scala.concurrent.Future
import etude.foundation.domain.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.account.{Account, AccountId}

trait AsyncAccountRepository
  extends AccountRepository[Future]
  with AsyncEntityReader[AccountId, Account] {

  type This <: AsyncAccountRepository
}
