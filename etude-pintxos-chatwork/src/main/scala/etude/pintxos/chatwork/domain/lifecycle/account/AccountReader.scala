package etude.pintxos.chatwork.domain.lifecycle.account

import etude.domain.core.lifecycle.{EntityIOContext, EntityReader}
import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}

import scala.language.higherKinds

private[account]
trait AccountReader[M[+A]]
  extends EntityReader[AccountId, Account, M] {

  def contacts()(implicit context: EntityIOContext[M]): M[List[Account]]

  def self()(implicit context: EntityIOContext[M]): M[Account]
}
