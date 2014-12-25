package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.V0AsyncApi
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.request.AddCategoryRequest
import etude.pintxos.chatwork.domain.infrastructure.api.v0.response.AddCategoryResponse
import etude.pintxos.chatwork.domain.model.room.Category
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

import scala.concurrent.Future

object AddCategory
  extends ChatWorkCommand[AddCategoryRequest, AddCategoryResponse] {


  def execute(request: AddCategoryRequest)(implicit context: EntityIOContext[Future]): Future[AddCategoryResponse] = {
    implicit val executor = getExecutionContext(context)

    val pdata = ("name" -> request.name) ~
      ("r" -> request.rooms.map(_.value.toString()))

    V0AsyncApi.api(
      "add_category",
      Map(),
      Map(
        "pdata" -> compact(render(pdata))
      )
    ) map {
      json =>
        AddCategoryResponse(
          json,
          CategoryParser.parseCategory(json).last
        )
    }
  }

}