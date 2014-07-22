package etude.pintxos.chatwork.domain.infrastructure.api.v0

import etude.manieres.domain.lifecycle.EntityIOContext
import org.json4s._

import scala.concurrent.Future

object V0AsyncUpdate
  extends V0AsyncEntityIO {

  def parseLastId(json: JValue): Option[String] = {
    val lastId: List[String] = for {
      JObject(doc) <- json
      JField("last_id", JString(id)) <- doc
    } yield {
      id
    }
    lastId.lastOption
  }

  def update(updateLastId: Boolean = true)(implicit context: EntityIOContext[Future]): Future[JValue] = {
    implicit val executor = getExecutionContext(context)

    getLastId(context) match {
      case Some(lastId) =>
        V0AsyncApi.api("get_update", Map("last_id" -> lastId)) map {
          json =>
            if (updateLastId) {
              parseLastId(json) match {
                case Some(newLastId) => setLastId(newLastId, context)
                case _ =>
              }
            }
            json
        }
      case _ =>
        throw new IllegalStateException("No last_id found in the context")
    }
  }
}
