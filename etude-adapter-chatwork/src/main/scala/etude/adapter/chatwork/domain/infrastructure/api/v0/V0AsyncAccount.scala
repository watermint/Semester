package etude.adapter.chatwork.domain.infrastructure.api.v0

import etude.domain.core.lifecycle.EntityIOContext
import etude.adapter.chatwork.domain.model.account.{Account, AccountId}
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
          account <- V0AsyncInitLoad.parseContact(contactDat)
        } yield {
          account
        }
    }
  }
}
