package etude.gazpacho.http

import org.apache.http.client.CookieStore
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.impl.client.BasicCookieStore

import scala.language.higherKinds

trait ClientContext {
  val cookieStore: CookieStore = new BasicCookieStore()

  val httpClientContext: HttpClientContext = HttpClientContext.create()
}
