package etude.messaging.chatwork.domain.lifecycle.account

import scala.concurrent.Future
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.messaging.chatwork.domain.model.account.{Account, AccountId}

trait AsyncAccountRepository
  extends AccountRepository[Future]
  with AsyncEntityReader[AccountId, Account] {

  type This <: AsyncAccountRepository
}

object AsyncAccountRepository {
  def ofV0Api(): AsyncAccountRepository =
    new AsyncAccountRepositoryOnV0Api

  def ofV1Api(): AsyncAccountRepository =
    new AsyncAccountRepositoryOnV1Api
}