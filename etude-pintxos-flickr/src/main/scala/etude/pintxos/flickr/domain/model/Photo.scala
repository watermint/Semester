package etude.pintxos.flickr.domain.model

import etude.manieres.domain.model.Entity

class Photo(val photoId: PhotoId,
             val title: Option[String] = None,
             val description: Option[String] = None) extends Entity[PhotoId] {
  val identity: PhotoId = photoId
}
