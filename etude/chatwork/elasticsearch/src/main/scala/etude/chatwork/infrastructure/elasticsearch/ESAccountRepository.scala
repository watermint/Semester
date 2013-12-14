package etude.chatwork.infrastructure.elasticsearch

import etude.chatwork.domain.account.{Account, AccountId, AccountRepository}
import scala.util.Try
import etude.elasticsearch.Engine

case class ESAccountRepository(implicit engine: Engine) extends AccountRepository {
  def me(): Try[Account] = ???

  def contacts(): Try[List[Account]] = ???

  def resolve(identifier: AccountId): Try[Account] = ???
}
