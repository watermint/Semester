package etude.elasticsearch

import scala.concurrent._
import org.elasticsearch.action.search.{SearchResponse, SearchRequestBuilder}
import org.elasticsearch.common.settings.ImmutableSettings

case class Index(indexName: String,
                 settingsJson: Option[String] = None)(implicit context: IOContext) {

  def exists: Future[Boolean] = {
    implicit val executor = context.executor
    future {
      context
        .engine
        .client
        .admin()
        .indices()
        .prepareExists(indexName)
        .execute()
        .get()
        .isExists
    }
  }

  def create: Future[Boolean] = {
    implicit val executor = context.executor
    future {
      val builder = context
        .engine
        .client
        .admin()
        .indices()
        .prepareCreate(indexName)

      (settingsJson match {
        case Some(json) => builder.setSettings(ImmutableSettings.settingsBuilder().loadFromSource(json))
        case _ => builder
      }).execute()
        .get()
        .isAcknowledged
    }
  }

  def delete: Future[Boolean] = {
    implicit val executor = context.executor
    future {
      context
        .engine
        .client
        .admin()
        .indices()
        .prepareDelete(indexName)
        .execute()
        .get()
        .isAcknowledged
    }
  }

  def flush: Future[Boolean] = {
    implicit val executor = context.executor
    future {
      context
        .engine
        .client
        .admin()
        .indices()
        .prepareFlush(indexName)
        .setFull(true)
        .execute()
        .get()
        .getFailedShards < 1
    }
  }

  def indexType(typeName: String): Type = {
    Type(indexName, typeName)
  }

  def searchRequestBuilder: SearchRequestBuilder = {
    implicit val executor = context.executor
    context
      .engine
      .client
      .prepareSearch(indexName)
  }

  def searchRequest(request: SearchRequestBuilder): Future[SearchResponse] = {
    implicit val executor = context.executor
    future {
      request
        .execute()
        .get()
    }
  }

}

object Index {
  def withKuromoji(indexName: String)(implicit context: IOContext): Index = {
    import org.json4s._
    import org.json4s.native._
    import org.json4s.native.Serialization.write

    implicit val formats = Serialization.formats(NoTypeHints)

    val settings = Map(
      "analysis" -> Map(
        "tokenizer" -> Map(
          "kuromoji" -> Map(
            "type" -> "kuromoji_tokenizer",
            "mode" -> "search"
          )
        )
      ),
      "analyzer" -> Map(
        "kuromoji_analyzer" -> Map(
          "type" -> "custom",
          "tokenizer" -> "kuromoji_tokenizer"
        )
      )
    )
    val settingJson = write(settings)

    Index(
      indexName = indexName,
      settingsJson = Some(settingJson)
    )
  }
}
