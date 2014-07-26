package etude.pintxos.flickr.domain.model

import etude.manieres.domain.model.Identity

case class PhotosetId(photosetId: String) extends Identity[String] {
  def value: String = photosetId
}
