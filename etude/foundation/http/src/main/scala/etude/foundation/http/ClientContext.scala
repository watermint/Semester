package etude.foundation.http

import scala.language.higherKinds
import org.apache.http.client.CookieStore
import org.apache.http.impl.client.{LaxRedirectStrategy, HttpClients, CloseableHttpClient, BasicCookieStore}
import org.apache.http.client.protocol.HttpClientContext

trait ClientContext {
  val cookieStore: CookieStore = new BasicCookieStore()

  val httpClientContext: HttpClientContext = HttpClientContext.create()

  val httpClient: CloseableHttpClient = HttpClients.custom()
    .setDefaultCookieStore(cookieStore)
    .setRedirectStrategy(new LaxRedirectStrategy)
    .build()
}
