package etude.pintxos.chatwork.domain.service.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.service.v0.Api
import etude.pintxos.chatwork.domain.service.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.service.v0.request.AddCategoryRequest
import etude.pintxos.chatwork.domain.service.v0.response.AddCategoryResponse
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object AddCategory
  extends ChatWorkCommand[AddCategoryRequest, AddCategoryResponse] {


  def execute(request: AddCategoryRequest)(implicit context: EntityIOContext[Future]): AddCategoryResponse = {
    implicit val executor = getExecutionContext(context)

    val pdata = ("name" -> request.name) ~
      ("r" -> request.rooms.map(_.value.toString()))

    val json = Api.api(
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