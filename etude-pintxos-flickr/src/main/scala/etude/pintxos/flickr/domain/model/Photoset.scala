package etude.pintxos.flickr.domain.model

import etude.manieres.domain.model.Entity

class Photoset(photosetId: PhotosetId,
                title: String,
                description: Option[String]) extends Entity[PhotosetId] {

  val identity: PhotosetId = photosetId
}
