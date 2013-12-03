package etude.chatwork.v1

import scala.util.Try

trait AccountRepository
  extends Repository[AccountId, Account]
  with EnumerableRepository[AccountId, Account] {

  def me(): Try[Account]

  def contacts(): Try[List[Account]]

  def contains(entity: Account): Try[Boolean] = contains(entity.accountId)

  def asEntitiesList: Try[List[Account]] = contacts()
}
