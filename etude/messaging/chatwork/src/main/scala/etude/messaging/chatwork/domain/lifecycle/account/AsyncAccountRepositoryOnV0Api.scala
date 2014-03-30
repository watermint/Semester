package etude.messaging.chatwork.domain.lifecycle.account

import etude.messaging.chatwork.domain.model.account.{AccountId, Account}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.messaging.chatwork.domain.infrastructure.NotImplementedException
import etude.messaging.chatwork.domain.infrastructure.v0.V0AsyncInitLoad

class AsyncAccountRepositoryOnV0Api extends AsyncAccountRepository {
  type This <: AsyncAccountRepositoryOnV0Api

  def self()(implicit context: EntityIOContext[Future]): Future[Account] =
    Future.failed(NotImplementedException("No implementation"))

  def containsByIdentity(identity: AccountId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.contacts.exists(_.accountId.equals(identity))
    }
  }

  def resolve(identity: AccountId)(implicit context: EntityIOContext[Future]): Future[Account] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.contacts.find(_.accountId.equals(identity)).last
    }
  }

  def contacts()(implicit context: EntityIOContext[Future]): Future[List[Account]] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.contacts
    }
  }
}
