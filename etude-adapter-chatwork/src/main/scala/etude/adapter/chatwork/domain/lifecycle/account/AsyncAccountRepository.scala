package etude.adapter.chatwork.domain.lifecycle.account

import etude.domain.core.lifecycle.EntityIOContext
import etude.domain.core.lifecycle.async.AsyncEntityReader
import etude.adapter.chatwork.domain.infrastructure.api.{AsyncEntityIOContextOnV0Api, AsyncEntityIOContextOnV1Api}
import etude.adapter.chatwork.domain.model.account.{Account, AccountId}

import scala.concurrent.Future

trait AsyncAccountRepository
  extends AccountRepository[Future]
  with AsyncEntityReader[AccountId, Account] {

  type This <: AsyncAccountRepository
}

object AsyncAccountRepository {
  def ofContext(context: EntityIOContext[Future]): AsyncAccountRepository = {
    context match {
      case c: AsyncEntityIOContextOnV0Api => ofV0Api()
      case c: AsyncEntityIOContextOnV1Api => ofV1Api()
      case _ => throw new IllegalArgumentException("Unsupported EntityIOContext")
    }
  }

  private def ofV0Api(): AsyncAccountRepository =
    new AsyncAccountRepositoryOnV0Api

  private def ofV1Api(): AsyncAccountRepository =
    new AsyncAccountRepositoryOnV1Api
}