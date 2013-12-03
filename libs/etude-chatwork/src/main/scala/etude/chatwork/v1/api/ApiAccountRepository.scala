package etude.chatwork.v1.api

import etude.chatwork.v1._
import java.net.URI
import java.time.Instant
import org.json4s._
import scala.collection.mutable
import scala.Some
import scala.util.{Failure, Try, Success}

case class ApiAccountRepository(implicit authToken: AuthToken) extends AccountRepository {
  private val ENDPOINT_ME = "/v1/me"

  private val ENDPOINT_CONTACTS = "/v1/contacts"

  private val lastLoad = mutable.HashMap[String, Instant]()

  private var cachedMe: Option[Account] = None

  private val cachedContacts = mutable.HashMap[AccountId, Account]()

  private val cachedAccounts = mutable.HashMap[AccountId, Account]()

  protected def parseAccount(result: JValue): List[Account] = {
    for {
      JObject(data) <- result
      JField("account_id", JInt(accountId)) <- data
      JField("name", JString(name)) <- data
      JField("chatwork_id", JString(chatworkId)) <- data
      JField("organization_id", JInt(organizationId)) <- data
      JField("organization_name", JString(organizationName)) <- data
      JField("department", JString(department)) <- data
      JField("avatar_image_url", JString(avatarImageUrl)) <- data
    } yield {
      Account(
        accountId = AccountId(accountId),
        name = name,
        chatWorkId = chatworkId match {
          case "" => None
          case c => Some(ChatWorkId(c))
        },
        organization = organizationId match {
          case id if id < 1 => None
          case _ =>
            Some(
              Organization(
                OrganizationId(organizationId),
                organizationName
              )
            )
        },
        department = department match {
          case "" => None
          case d => Some(d)
        },
        avatarImage = new URI(avatarImageUrl)
      )
    }
  }

  protected def shouldFail(operation: String): Boolean = {
    lastLoad.getOrElse(operation, Instant.EPOCH)
      .plusSeconds(Api.QOS_RETRY_DURATION_OF_FAILURE)
      .isAfter(Instant.now)
  }

  protected def loadMe(): Try[Account] = {
    if (shouldFail(ENDPOINT_ME)) {
      return Failure(QoSException(ENDPOINT_ME))
    }

    try {
      Api.get(ENDPOINT_ME) match {
        case Success(r) => parseAccount(r).lastOption match {
          case Some(m) =>
            cachedMe = Some(m)
            Success(m)
          case _ =>
            Failure(ApiException("Unknown chatwork protocol"))
        }
        case Failure(f) => Failure(f)
      }
    } finally {
      lastLoad.put(ENDPOINT_ME, Instant.now)
    }
  }

  def loadContacts(): Try[List[Account]] = {
    if (shouldFail(ENDPOINT_CONTACTS)) {
      return Failure(QoSException(ENDPOINT_CONTACTS))
    }

    try {
      Api.get(ENDPOINT_CONTACTS) match {
        case Success(r) =>
          val contacts = parseAccount(r)
          contacts.foreach(c => cachedContacts.put(c.accountId, c))
          contacts.foreach(c => cachedAccounts.put(c.accountId, c))
          Success(contacts)
        case Failure(f) => Failure(f)
      }
    } finally {
      lastLoad.put(ENDPOINT_CONTACTS, Instant.now)
    }
  }

  def me(): Try[Account] = {
    cachedMe match {
      case Some(m) => Success(m)
      case _ => loadMe()
    }
  }

  def contacts(): Try[List[Account]] = {
    lastLoad.get(ENDPOINT_CONTACTS) match {
      case Some(c) => Success(cachedContacts.values.toList)
      case _ => loadContacts()
    }
  }

  def resolve(identifier: AccountId): Try[Account] = {
    contacts() match {
      case Success(c) => c.find(_.accountId == identifier) match {
        case Some(a) => Success(a)
        case _ => Failure(NotFoundException("Account not found for [" + identifier.id + "]"))
      }
      case _ => Failure(NotFoundException("Account not found for [" + identifier.id + "]"))
    }
  }

  def asEntitiesList: Try[List[Account]] = contacts()

  def contains(identifier: AccountId): Try[Boolean] = {
    resolve(identifier) match {
      case Success(s) => Success(true)
      case Failure(f) => Failure(f)
    }
  }

  def contains(entity: Account): Try[Boolean] = contains(entity.accountId)
}
