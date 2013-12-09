package etude.chatwork.infrastructure.api.v1

import java.net.URI
import java.time.Instant
import org.json4s._
import scala.util.Try
import etude.chatwork.domain._
import etude.chatwork.infrastructure.api.{QoSException, ApiQoS}
import etude.chatwork.domain.account._
import scala.util.Failure
import scala.Some
import scala.util.Success
import etude.commons.domain.EntityNotFoundException

case class V1AccountRepository(implicit authToken: V1AuthToken) extends AccountRepository with ApiQoS {
  private val ENDPOINT_ME = "/v1/me"

  private val ENDPOINT_CONTACTS = "/v1/contacts"

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
      new Account(
        accountId = new AccountId(accountId),
        name = Some(name),
        chatWorkId = chatworkId match {
          case "" => None
          case c => Some(new ChatWorkId(c))
        },
        organization = organizationId match {
          case id if id < 1 => None
          case _ =>
            Some(
              new Organization(
                new OrganizationId(organizationId),
                organizationName
              )
            )
        },
        department = department match {
          case "" => None
          case d => Some(d)
        },
        avatarImage = Some(new URI(avatarImageUrl))
      )
    }
  }

  def me(): Try[Account] = {
    if (shouldFail(ENDPOINT_ME)) {
      return Failure(QoSException(ENDPOINT_ME))
    }

    try {
      V1Api.get(ENDPOINT_ME) match {
        case Failure(f) => Failure(f)
        case Success(r) => parseAccount(r).lastOption match {
          case Some(m) => Success(m)
          case _ => Failure(V1ApiException("Unknown chatwork protocol"))
        }
      }
    } finally {
      lastLoad.put(ENDPOINT_ME, Instant.now)
    }
  }

  def contacts(): Try[List[Account]] = {
    if (shouldFail(ENDPOINT_CONTACTS)) {
      return Failure(QoSException(ENDPOINT_CONTACTS))
    }

    try {
      V1Api.get(ENDPOINT_CONTACTS) match {
        case Success(r) => Success(parseAccount(r))
        case Failure(f) => Failure(f)
      }
    } finally {
      lastLoad.put(ENDPOINT_CONTACTS, Instant.now)
    }
  }

  def resolve(identifier: AccountId): Try[Account] = {
    contacts() match {
      case Success(c) => c.find(_ == identifier) match {
        case Some(a) => Success(a)
        case _ => Failure(EntityNotFoundException(s"Account not found for ${identifier.value}"))
      }
      case _ => Failure(EntityNotFoundException(s"Account not found for ${identifier.value}"))
    }
  }
}
