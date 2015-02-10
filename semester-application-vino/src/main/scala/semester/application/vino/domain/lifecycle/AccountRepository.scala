package semester.application.vino.domain.lifecycle

import java.net.URI

import semester.service.chatwork.domain.model.account.{Account, AccountId}
import semester.application.vino.domain.infrastructure.ElasticSearch
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

case class AccountRepository(engine: ElasticSearch) extends SimpleIndexRepository[Account, AccountId] {

  val indexName: String = "cw-account"

  val typeName: String = "account"

  def fromJsonSeq(id: Option[String], source: JValue): Seq[Account] = {
    for {
      JObject(o) <- source
      JField("accountId", JInt(accountId)) <- o
      JField("name", JString(name)) <- o
      JField("department", JString(department)) <- o
      JField("avatarUrl", JString(avatar)) <- o
    } yield {
      new Account(
        accountId = AccountId(accountId),
        name = name match {
          case null | "" => None
          case n => Some(n)
        },
        department = department match {
          case null | "" => None
          case d => Some(d)
        },
        avatarImage = avatar match {
          case null | "" => None
          case u => Some(new URI(u))
        }
      )
    }
  }

  def toJson(entity: Account): JValue = {
    ("accountId" -> entity.accountId.value) ~
      ("name" -> entity.name.getOrElse("")) ~
      ("department" -> entity.department.getOrElse("")) ~
      ("avatarUrl" -> entity.avatarImage.getOrElse(new URI("")).toString)
  }

  def toIdentity(identity: AccountId): String = identity.value.toString()
}
