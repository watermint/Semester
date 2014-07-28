package etude.pintxos.flickr.domain.lifecycle

import java.io.InputStream

import etude.manieres.domain.lifecycle.{Repository, ResultWithIdentity, EntityIOContext, EntityReader}
import etude.pintxos.flickr.domain.model._

import scala.language.higherKinds

trait PhotoRepository[M[+A]] extends Repository[PhotoId, Photo, M] {

  type This <: PhotoRepository[M]

  def create(title: String,
             description: Option[String] = None,
             tags: Seq[String] = Seq(),
             permission: Visibility = VisibilityPrivate(),
             safety: Safety = SafetySafe(),
             contentType: ContentType = ContentTypePhoto(),
             photo: InputStream)(implicit context: EntityIOContext[M]): M[ResultWithIdentity[This, PhotoId, Photo, M]]
}
