package etude.vino.chatwork.domain.lifecycle

import java.net.URI

import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}
import org.json4s.JsonDSL._
import org.json4s.{JField, JInt, JObject, JString, JValue}

object AccountRepository extends SimpleIndexRepository[Account, AccountId] {

  val indexName: String = "cw-account"

  val typeName: String = "account"

  def fromJsonSeq(json: JValue): Seq[Account] = {
    for {
      JObject(o) <- json
      JField("_source", JObject(source)) <- o
      JField("accountId", JInt(accountId)) <- source
      JField("name", JString(name)) <- source
      JField("department", JString(department)) <- source
      JField("avatarUrl", JString(avatar)) <- source
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
