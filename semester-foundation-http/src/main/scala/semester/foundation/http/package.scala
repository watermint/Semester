package semester.foundation

import java.net.URI

package object http {
  implicit def uriToUriContainer(uri: URI): URIContainer = URIContainer(uri)

  implicit def uriContainerToUri(uriContainer: URIContainer): URI = uriContainer.uri
}
