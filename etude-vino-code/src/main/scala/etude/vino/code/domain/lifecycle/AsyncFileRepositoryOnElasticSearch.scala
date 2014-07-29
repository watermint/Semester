package etude.vino.code.domain.lifecycle

import etude.manieres.domain.lifecycle.async.AsyncEntityIO
import etude.pain.elasticsearch.domain.lifecycle.AsyncRepositoryOnElasticSearch
import etude.vino.code.domain.model.{File, FileId}
import etude.pain.elasticsearch.Engine
import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

private[lifecycle]
class AsyncFileRepositoryOnElasticSearch(val engine: Engine)
  extends AsyncFileRepository
  with AsyncRepositoryOnElasticSearch[FileId, File]
  with AsyncEntityIO {

  type This <: AsyncFileRepositoryOnElasticSearch

  val typeValue: String = "file"

  def indexValue(identity: FileId): String = "file"

  def idValue(identity: FileId): String = identity.path

  def marshal(entity: File): String = {
    compact(
      render(
        ("path" -> entity.identity.path) ~
          ("contentType" -> entity.contentType)
      )
    )
  }

  def unmarshal(json: String): File = {
    val p = parse(json)
    val results: Seq[File] = for {
      JObject(j) <- p
      JField("path", JString(path)) <- j
      JField("contentType", JString(contentType)) <- j
    } yield {
      new File(
        fileId = FileId(path),
        contentType = contentType
      )
    }
    results.last
  }
}
