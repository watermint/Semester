package etude.messaging.chatwork.domain.account

import scala.language.higherKinds
import etude.foundation.domain.lifecycle.{EntityIOContext, EntityReader}

private[account]
trait AccountReader[M[+A]]
  extends EntityReader[AccountId, Account, M] {

  def contacts()(implicit context: EntityIOContext[M]): M[List[Account]]

  def self()(implicit context: EntityIOContext[M]): M[Account]
}
