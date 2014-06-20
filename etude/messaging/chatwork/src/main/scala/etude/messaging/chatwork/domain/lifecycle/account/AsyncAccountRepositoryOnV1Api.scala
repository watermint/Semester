package etude.messaging.chatwork.domain.lifecycle.account

import java.net.URI

import etude.domain.core.lifecycle.{EntityIOContext, EntityNotFoundException}
import etude.messaging.chatwork.domain.infrastructure.api.v1.V1AsyncApi
import etude.messaging.chatwork.domain.model.account._
import org.json4s._

import scala.concurrent.Future

class AsyncAccountRepositoryOnV1Api
  extends AsyncAccountRepository {

  type This <: AsyncAccountRepositoryOnV1Api

  protected val ENDPOINT_ME = "/v1/me"

  protected val ENDPOINT_CONTACTS = "/v1/contacts"

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
        accountId = AccountId(accountId),
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

  def self()(implicit context: EntityIOContext[Future]): Future[Account] = {
    implicit val executor = getExecutionContext(context)
    V1AsyncApi.get(ENDPOINT_ME) map {
      json =>
        parseAccount(json).last
    }
  }

  def contacts()(implicit context: EntityIOContext[Future]): Future[List[Account]] = {
    implicit val executor = getExecutionContext(context)
    V1AsyncApi.get(ENDPOINT_CONTACTS) map {
      json =>
        parseAccount(json)
    }
  }

  def resolve(identity: AccountId)(implicit context: EntityIOContext[Future]): Future[Account] = {
    implicit val executor = getExecutionContext(context)
    contacts() map {
      accounts =>
        accounts.find(_ == identity) match {
          case Some(a) => a
          case _ => throw EntityNotFoundException(s"Account not found for ${identity.value}")
        }
    }
  }

  def containsByIdentity(identity: AccountId)(implicit context: EntityIOContext[Future]): Future[Boolean] = {
    implicit val executor = getExecutionContext(context)
    contacts() map {
      accounts =>
        accounts.find(_ == identity) match {
          case Some(a) => true
          case _ => false
        }
    }
  }
}
