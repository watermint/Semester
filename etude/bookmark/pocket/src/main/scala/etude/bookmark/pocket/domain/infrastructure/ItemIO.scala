package etude.bookmark.pocket.domain.infrastructure

import scala.concurrent.Future
import etude.foundation.http.{AsyncClientContext, AsyncClient}
import java.net.{URL, URI}
import etude.bookmark.pocket.domain.model.{ItemEntry, Item}

object ItemIO {

  def addItem(item: ItemEntry)(implicit context: EntityIOContextOnApi): Future[Item] = {
    implicit val executionContext = context.executionContext
    import org.json4s._
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val client = AsyncClient(AsyncClientContext(executionContext))
    val endPoint = "https://getpocket.com/v3/add"
    val requestContent: Map[String, String] = (
      Seq("url" -> item.url.toString) ++
        (item.title match {
          case Some(t) => Seq("title" -> "")
          case _ => Seq.empty
        }) ++
        (item.tweetId match {
          case Some(t) => Seq("tweet_id" -> "")
          case _ => Seq.empty
        }) ++
        (item.tags match {
          case Seq() => Seq.empty
          case t => Seq("tags" -> t.mkString(","))
        })
      ).toMap

    client.postWithString(
      new URI(endPoint),
      compact(render(requestContent))
    ) map {
      response =>
        val json = response.contentAsJson.get
        val result: Seq[Item] = for {
          JObject(r) <- json
          JField("item", JObject(item)) <- r
          JField("item_id", JString(itemId)) <- item
          JField("normal_url", JString(normalUrl)) <- item
          JField("resolved_id", JString(resolvedId)) <- item
          JField("resolved_url", JString(resolvedUrl)) <- item
          JField("mime_type", JString(mimeType)) <- item
        } yield {
          Item(
            itemId = itemId,
            normalUrl = new URL(normalUrl),
            resolvedId = resolvedId,
            resolvedUrl = new URL(resolvedId),
            mimeType = mimeType
          )
        }
        result.last
    }
  }
}