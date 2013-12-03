package etude.chatwork.v1

import scala.util.Try

trait AccountRepository extends Repository[AccountId, Account] {
  def me(): Try[Account]

  def contacts(): Try[List[Account]]
}
