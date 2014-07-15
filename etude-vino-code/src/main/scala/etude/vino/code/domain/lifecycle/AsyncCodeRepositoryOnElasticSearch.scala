package etude.vino.code.domain.lifecycle

import etude.domain.elasticsearch.AsyncRepositoryOnElasticSearch
import etude.vino.code.domain.model.{FileId, Code}
import etude.gazpacho.elasticsearch.Engine
import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

private[lifecycle]
class AsyncCodeRepositoryOnElasticSearch(val engine: Engine)
  extends AsyncCodeRepository
  with AsyncRepositoryOnElasticSearch[FileId, Code] {

  type This <: AsyncCodeRepositoryOnElasticSearch

  def indexValue(identity: FileId): String = "code"

  def idValue(identity: FileId): String = identity.path

  val typeValue: String = "code"

  def marshal(entity: Code): String = {
    compact(
      render(
        ("path" -> entity.identity.path) ~
          ("code" -> entity.code) ~
          ("highlight" -> entity.highlight)
      )
    )
  }

  def unmarshal(json: String): Code = {
    val p = parse(json)
    val results: Seq[Code] = for {
      JObject(j) <- p
      JField("path", JString(path)) <- j
      JField("code", JString(code)) <- j
      JField("highlight", JString(highlight)) <- j
    } yield {
      new Code(
        fileId = new FileId(path),
        code = code,
        highlight = highlight
      )
    }
    results.last
  }
}
