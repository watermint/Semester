package semester.foundation.http

import org.apache.http.client.CookieStore
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.impl.client.BasicCookieStore

import scala.language.higherKinds

case class ClientContext(cookieStore: CookieStore = new BasicCookieStore(),
                         httpClientContext: HttpClientContext = HttpClientContext.create())

