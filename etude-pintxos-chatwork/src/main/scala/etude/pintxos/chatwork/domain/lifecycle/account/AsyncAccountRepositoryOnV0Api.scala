package etude.pintxos.chatwork.domain.lifecycle.account

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncInitLoad
import etude.pintxos.chatwork.domain.infrastructure.api.v0.command.GetAccountInfo
import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}

import scala.concurrent.Future

class AsyncAccountRepositoryOnV0Api extends AsyncAccountRepository {
  type This <: AsyncAccountRepositoryOnV0Api

  def self()(implicit context: EntityIOContext[Future]): Future[Account] =
    Future.failed(new UnsupportedOperationException("No implementation"))

  def containsByIdentity(identity: AccountId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() map {
      p =>
        p.contacts.exists(_.accountId.equals(identity))
    }
  }

  def resolve(identity: AccountId)(implicit context: EntityIOContext[Future]): Future[Account] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncInitLoad.initLoad() flatMap {
      p =>
        GetAccountInfo.accounts(Seq(identity)) map {
          a =>
            a.last
        }
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
