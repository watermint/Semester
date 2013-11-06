package etude

import java.net.{URL, URI}

/**
 *
 */
package object http {
  implicit def uriToResource(uri: URI): Resource = Resource(uri)

  implicit def urlToResource(url: URL): Resource = Resource(url.toURI)
}
