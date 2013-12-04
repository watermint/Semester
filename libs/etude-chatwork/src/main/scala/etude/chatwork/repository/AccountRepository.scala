package etude.chatwork.repository

import scala.util.Try
import etude.chatwork.model.{AccountId, Account}

trait AccountRepository
  extends Repository[AccountId, Account]
  with EnumerableRepository[AccountId, Account] {

  def me(): Try[Account]

  def contacts(): Try[List[Account]]

  def contains(entity: Account): Try[Boolean] = contains(entity.accountId)

  def asEntitiesList: Try[List[Account]] = contacts()
}
