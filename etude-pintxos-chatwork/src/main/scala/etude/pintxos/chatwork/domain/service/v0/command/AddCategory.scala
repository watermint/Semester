package etude.pintxos.chatwork.domain.service.v0.command

import etude.pintxos.chatwork.domain.service.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.service.v0.request.AddCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.AddCategoryResponse
import etude.pintxos.chatwork.domain.service.v0.{ChatWorkApi, ChatWorkIOContext}
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object AddCategory
  extends ChatWorkCommand[AddCategoryRequest, AddCategoryResponse] {

  def execute(request: AddCategoryRequest)(implicit context: ChatWorkIOContext): AddCategoryResponse = {
    val pdata = ("name" -> request.name) ~
      ("r" -> request.rooms.map(_.value.toString()))

    val json = ChatWorkApi.api(
      "add_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    )

    AddCategoryResponse(
      json,
      CategoryParser.parseCategory(json).last
    )
  }

}