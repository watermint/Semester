package etude.pintxos.flickr.domain.lifecycle

import etude.manieres.domain.lifecycle.Repository
import etude.pintxos.flickr.domain.model.{Photoset, PhotosetId}

import scala.language.higherKinds

trait PhotosetRepository[M[+A]] extends Repository[PhotosetId, Photoset, M] {

}
