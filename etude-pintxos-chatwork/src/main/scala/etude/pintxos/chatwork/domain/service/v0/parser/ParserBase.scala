package etude.pintxos.chatwork.domain.service.v0.parser

import java.net.URI

import etude.pintxos.chatwork.domain.model.account.{OrganizationId, Organization}

trait ParserBase {
  val accountIconUrlBase = "https://tky-chat-work-appdata.s3.amazonaws.com/avatar/"

  val roomIconUrlBase = "https://tky-chat-work-appdata.s3.amazonaws.com/icon/"

  def asOptionOrganization(orgId: BigInt, orgName: String): Option[Organization] = {
    Some(
      new Organization(
        OrganizationId(orgId),
        orgName
      )
    )
  }

  def asOptionString(value: String): Option[String] = {
    if (value == null || "".equals(value)) {
      None
    } else {
      Some(value)
    }
  }

  def asOptionURI(base: String, part: String): Option[URI] = {
    asOptionString(part) map {
      v =>
        new URI(base + v)
    }
  }

}
