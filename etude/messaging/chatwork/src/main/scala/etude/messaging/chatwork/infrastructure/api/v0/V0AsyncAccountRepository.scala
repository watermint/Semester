package etude.messaging.chatwork.infrastructure.api.v0

import etude.messaging.chatwork.domain.account.{AccountId, Account, AsyncAccountRepository}
import etude.foundation.domain.lifecycle.EntityIOContext
import scala.concurrent.Future
import etude.messaging.chatwork.infrastructure.api.NotImplementedException

class V0AsyncAccountRepository extends AsyncAccountRepository {
  type This <: V0AsyncAccountRepository

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
