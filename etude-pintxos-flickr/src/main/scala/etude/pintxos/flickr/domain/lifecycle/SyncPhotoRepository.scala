package etude.pintxos.flickr.domain.lifecycle

import java.io.InputStream

import etude.manieres.domain.lifecycle.{ResultWithIdentity, EntityIOContext}
import etude.manieres.domain.lifecycle.sync.SyncRepository
import etude.pintxos.flickr.domain.model._

import scala.util.Try

case class SyncPhotoRepository()
  extends PhotoRepository[Try]
  with SyncRepository[PhotoId, Photo] {
  type This <: SyncPhotoRepository

  def create(title: String,
             description: Option[String],
             tags: Seq[String],
             permission: Visibility,
             safety: Safety,
             contentType: ContentType,
             photo: InputStream)
            (implicit context: EntityIOContext[Try]): Try[ResultWithIdentity[This, PhotoId, Photo, Try]] = {
    ???
  }

  def containsByIdentity(identity: PhotoId)
                        (implicit context: EntityIOContext[Try]): Try[Boolean] = {
    ???
  }

  def resolve(identity: PhotoId)
             (implicit context: EntityIOContext[Try]): Try[Photo] = {
    ???
  }

  def store(entity: Photo)
           (implicit context: EntityIOContext[Try]): Try[ResultWithIdentity[This, PhotoId, Photo, Try]] = {
    throw new UnsupportedOperationException("Use `create` instead")
  }

  def deleteByIdentity(identity: PhotoId)
                      (implicit context: EntityIOContext[Try]): Try[ResultWithIdentity[This, PhotoId, Photo, Try]] = {
    ???
  }
}
