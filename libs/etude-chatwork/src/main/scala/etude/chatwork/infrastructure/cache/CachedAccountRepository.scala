package etude.chatwork.infrastructure.cache

import scala.util.{Failure, Success, Try}
import etude.chatwork.domain.account.{AccountRepository, AccountId, Account}

case class CachedAccountRepository(repository: AccountRepository)
  extends AccountRepository
  with CacheQoS[AccountId, Account] {

  private var cachedMe: Option[Account] = None

  def me(): Try[Account] = {
    cachedMe match {
      case Some(cm) => Success(cm)
      case _ => repository.me() match {
        case Failure(f) => Failure(f)
        case Success(a) =>
          cachedMe = Some(a)
          Success(a)
      }
    }
  }

  def contacts(): Try[List[Account]] = {
    entityListOperation(repository.contacts())
  }

  def resolve(identifier: AccountId): Try[Account] = {
    entityResolveOperation(identifier)(repository.resolve(identifier))
  }
}
