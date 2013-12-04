package etude.chatwork.repository.cache

import scala.util.{Failure, Success, Try}
import etude.chatwork.model.{AccountId, Account}
import etude.chatwork.repository.AccountRepository
import etude.chatwork.repository.api.v1.ApiAccountRepository

case class CachedAccountRepository(api: ApiAccountRepository)
  extends AccountRepository
  with CacheQoS[AccountId, Account] {

  private var cachedMe: Option[Account] = None

  def me(): Try[Account] = {
    cachedMe match {
      case Some(cm) => Success(cm)
      case _ => api.me() match {
        case Failure(f) => Failure(f)
        case Success(a) =>
          cachedMe = Some(a)
          Success(a)
      }
    }
  }

  def contacts(): Try[List[Account]] = {
    entityListOperation(api.contacts())
  }

  def resolve(identifier: AccountId): Try[Account] = {
    entityResolveOperation(identifier)(api.resolve(identifier))
  }
}
