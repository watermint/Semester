package etude.data.chintai.domain

import etude.foundation.domain.model.Identity
import java.net.URL

case class PropertyId(url: URL)
  extends Identity[URL] {

  override def value: URL = url
}
