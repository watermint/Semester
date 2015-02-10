package semester.service.chatwork.domain.service.command

import org.json4s.JsonAST.{JField, JObject}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import semester.service.chatwork.domain.service.parser.ContactParser
import semester.service.chatwork.domain.service.request.GetAccountInfoRequest
import semester.service.chatwork.domain.service.response.GetAccountInfoResponse
import semester.service.chatwork.domain.service.{ChatWorkApi, ChatWorkIOContext}

object GetAccountInfo
  extends ChatWorkCommand[GetAccountInfoRequest, GetAccountInfoResponse] {


  def execute(request: GetAccountInfoRequest)(implicit context: ChatWorkIOContext): GetAccountInfoResponse = {
    val json = ChatWorkApi.api(
      "get_account_info",
      Map(),
      Map(
        "pdata" -> compact(render("aid" -> request.accountIds.map(_.value)))
      )
    )

    GetAccountInfoResponse(
      json,
      request,
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
