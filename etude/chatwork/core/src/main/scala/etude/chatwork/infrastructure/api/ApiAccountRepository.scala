package etude.chatwork.infrastructure.api

import scala.util.Try
import etude.chatwork.domain.account.{Account, AccountId, AccountRepository}
import etude.chatwork.infrastructure.api.v1.V1AccountRepository

case class ApiAccountRepository(implicit authContext: MixedAuthContext) extends AccountRepository {
  def me(): Try[Account] = V1AccountRepository().me()

  def contacts(): Try[List[Account]] = V1AccountRepository().contacts()

  def resolve(identifier: AccountId): Try[Account] = V1AccountRepository().resolve(identifier)
}
