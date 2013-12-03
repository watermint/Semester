package etude.chatwork.v1.cached

import etude.chatwork.v1.{EntityNotFoundException, AccountId, Account, AccountRepository}
import etude.chatwork.v1.api.ApiAccountRepository
import scala.util.{Failure, Success, Try}

case class CachedAccountRepository(api: ApiAccountRepository) extends AccountRepository {

  private var cachedMe: Option[Account] = None

  private var cachedContacts: Option[Map[AccountId, Account]] = None

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
    cachedContacts match {
      case Some(cc) => Success(cc.values.toList)
      case _ => api.contacts() match {
        case Failure(f) => Failure(f)
        case Success(c) =>
          cachedContacts = Some(c.map(a => a.accountId -> a).toMap)
          Success(c)
      }
    }
  }

  def resolve(identifier: AccountId): Try[Account] = {
    contacts() match {
      case Failure(f) => Failure(f)
      case Success(c) =>
        cachedContacts.getOrElse(identifier, Map[AccountId, Account]()) match {
          case Some(a: Account) => Success(a)
          case _ => Failure(EntityNotFoundException("Account not found for identifier: " + identifier))
        }
    }
  }
}
