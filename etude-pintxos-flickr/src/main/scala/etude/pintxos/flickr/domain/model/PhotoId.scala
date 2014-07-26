package etude.pintxos.flickr.domain.model

import etude.manieres.domain.model.Identity

case class PhotoId(photoId: String) extends Identity[String] {
  def value: String = photoId
}
