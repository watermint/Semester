package etude.epice

import java.net.{URI, URL}

package object http {
  implicit def uriToResource(uri: URI): Resource = Resource(uri)

  implicit def urlToResource(url: URL): Resource = Resource(url.toURI)

  implicit def uriToUriContainer(uri: URI): URIContainer = URIContainer(uri)

  implicit def uriContainerToUri(uriContainer: URIContainer): URI = uriContainer.uri
}
