package semester.service.chatwork.domain.service.v0.parser

import semester.service.chatwork.domain.model.account.{Account, AccountId, ChatWorkId}
import org.json4s.JsonAST.{JField, JInt, JObject, JString}
import org.json4s._

object ContactParser extends ParserBase {

  def parseContact(contactDat: List[(String, JValue)]): List[Account] = {
    for {
      JField(accountId, JObject(contact)) <- contactDat
      JField("av", JString(avatarImage)) <- contact
      JField("cwid", JString(chatWorkId)) <- contact
      JField("gid", JInt(organizationId)) <- contact
      JField("onm", JString(organization)) <- contact
      JField("dp", JString(department)) <- contact
      JField("name", JString(name)) <- contact
    } yield {
      new Account(
        accountId = AccountId(BigInt(accountId)),
        name = asOptionString(name),
        chatWorkId = asOptionString(chatWorkId).map(ChatWorkId),
        organization = asOptionOrganization(organizationId, organization),
        department = asOptionString(department),
        avatarImage = asOptionURI(accountIconUrlBase, avatarImage)
      )
    }
  }

  def parseContacts(json: JValue): List[Account] = {
    for {
      JObject(doc) <- json
      JField("contact_dat", JObject(contactDat)) <- doc
      account <- parseContact(contactDat)
    } yield {
      account
    }
  }
}
