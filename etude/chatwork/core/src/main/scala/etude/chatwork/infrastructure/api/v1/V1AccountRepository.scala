package etude.chatwork.infrastructure.api.v1

import java.net.URI
import org.json4s._
import etude.chatwork.infrastructure.api.ApiQoS
import etude.chatwork.domain.account._
import scala.Some
import etude.foundation.domain.lifecycle.{EntityIOContext, EntityNotFoundException}
import scala.concurrent.Future

case class V1AccountRepository
  extends AsyncAccountRepository
  with ApiQoS {

  type This <: V1AccountRepository

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

  //  def me(): Try[Account] = {
  //    if (shouldFail(ENDPOINT_ME)) {
  //      return Failure(QoSException(ENDPOINT_ME))
  //    }
  //
  //    try {
  //      V1AsyncApi.get(ENDPOINT_ME) match {
  //        case Failure(f) => Failure(f)
  //        case Success(r) => parseAccount(r).lastOption match {
  //          case Some(m) => Success(m)
  //          case _ => Failure(V1ApiException("Unknown chatwork protocol"))
  //        }
  //      }
  //    } finally {
  //      lastLoad.put(ENDPOINT_ME, Instant.now)
  //    }
  //  }
  //

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
