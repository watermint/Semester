package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.ContactParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.GetAccountInfoRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.GetAccountInfoResponse
import org.json4s.JsonAST.{JField, JObject}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object GetAccountInfo
  extends ChatWorkCommand[GetAccountInfoRequest, GetAccountInfoResponse] {


  def execute(request: GetAccountInfoRequest)(implicit context: EntityIOContext[Future]): Future[GetAccountInfoResponse] = {
    implicit val executor = getExecutionContext(context)

    V0AsyncApi.api(
      "get_account_info",
      Map(),
      Map(
        "pdata" -> compact(render("aid" -> request.accountIds.map(_.value)))
      )
    ) map {
      json =>
        GetAccountInfoResponse(
          json,
          for {
            JObject(doc) <- json
            JField("account_dat", JObject(contactDat)) <- doc
            account <- ContactParser.parseContact(contactDat)
          } yield {
            account
          }
        )
    }
  }
}
