package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.ContactParser
import etude.pintxos.chatwork.domain.model.account.{Account, AccountId}
import org.json4s.JsonAST.{JField, JObject}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object V0AsyncAccount
  extends V0AsyncEntityIO {

  def accounts(accountIds: Seq[AccountId])
              (implicit context: EntityIOContext[Future]): Future[Seq[Account]] = {

    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api(
      "get_account_info",
      Map(),
      Map(
        "pdata" -> compact(render("aid" -> accountIds.map(_.value)))
      )
    ) map {
      json =>
        for {
          JObject(doc) <- json
          JField("account_dat", JObject(contactDat)) <- doc
          account <- ContactParser.parseContact(contactDat)
        } yield {
          account
        }
    }
  }
}
