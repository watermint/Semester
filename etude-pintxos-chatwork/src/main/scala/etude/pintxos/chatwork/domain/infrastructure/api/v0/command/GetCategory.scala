package etude.pintxos.chatwork.domain.infrastructure.api.v0.command

import etude.manieres.domain.lifecycle.EntityIOContext
import etude.pintxos.chatwork.domain.infrastructure.api.v0.parser.CategoryParser
import etude.pintxos.chatwork.domain.infrastructure.api.v0.{V0AsyncApi, V0AsyncEntityIO}
import etude.pintxos.chatwork.domain.model.room.Category

import scala.concurrent.Future

object GetCategory
  extends V0AsyncEntityIO {

  def categories()(implicit context: EntityIOContext[Future]): Future[List[Category]] = {
    implicit val executor = getExecutionContext(context)
    V0AsyncApi.api(
      "get_category",
      Map()
    ) map {
      json =>
        CategoryParser.parseCategory(json)
    }
  }
}
