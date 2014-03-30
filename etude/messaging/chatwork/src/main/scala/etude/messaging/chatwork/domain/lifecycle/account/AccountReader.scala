package etude.messaging.chatwork.domain.lifecycle.account

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.{EntityIOContext, EntityReader}
import etude.messaging.chatwork.domain.model.account.{Account, AccountId}

private[account]
trait AccountReader[M[+A]]
  extends EntityReader[AccountId, Account, M] {

  def contacts()(implicit context: EntityIOContext[M]): M[List[Account]]

  def self()(implicit context: EntityIOContext[M]): M[Account]
}
