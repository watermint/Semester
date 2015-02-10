package semester.application.vino.domain.lifecycle

import java.time.Instant

import semester.application.vino.domain.Models
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s.JsonDSL._

object SystemRepository {
  val indexName = "cw-vino"

  def vinoVersion(): Long = {
    Models.engine.client.prepareIndex()
      .setIndex(indexName)
      .setType("main")
      .setId("main")
      .setSource( s"""{"@timestamp":"${Instant.now.toString}"}""")
      .get()
      .getVersion
  }

  def updateLastId(lastId: String): Unit = {
    val data = ("lastId" -> lastId) ~
      ("@timestamp" -> Instant.now.toString)

    Models.engine.update(indexName, "lastId", "0", data)
  }

  def lastId(): Option[String] = {
    Models.engine.get(indexName, "lastId", "0") map {
      json =>
        val result: Seq[String] = for {
          JObject(o) <- json
          JField("lastId", JString(lastId)) <- o
        } yield {
          lastId
        }
        result.last
    }
  }
}
