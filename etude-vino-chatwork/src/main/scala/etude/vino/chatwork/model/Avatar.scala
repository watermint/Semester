package etude.vino.chatwork.model

import java.net.URI
import java.util.concurrent.locks.ReentrantLock

import etude.manieres.domain.model.Identity
import etude.vino.chatwork.ui.UIStyles

import scala.collection.mutable
import scalafx.scene.Node
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle

class Avatar[ID <: Identity[_]] {
  private val avatarUrls = new mutable.HashMap[ID, String]()

  private val avatars = new mutable.HashMap[ID, Image]()

  private val avatarUpdateLock = new ReentrantLock()

  def nodeForEmpty() = new Rectangle {
    width = UIStyles.avatarThumbnail
    height = UIStyles.avatarThumbnail
    fill = Color.Gray
  }

  def nodeForImage(avatar: Image) = new ImageView {
    fitWidth = UIStyles.avatarThumbnail
    fitHeight = UIStyles.avatarThumbnail
    image = avatar
  }

  def nodeOf(identity: ID): Node = {
    avatars.get(identity) match {
      case Some(avatar) => nodeForImage(avatar)
      case None => nodeForEmpty()
    }
  }

  def nodeOf(identity: Option[ID]): Node = {
    identity match {
      case Some(id) => nodeOf(id)
      case None => nodeForEmpty()
    }
  }

  def updateAvatar(identity: ID, imageUrl: Option[URI]): Unit ={
    imageUrl match {
      case Some(url) => updateAvatar(identity, url.toString)
      case None =>
    }
  }

  def updateAvatar(identity: ID, imageUrl: String): Unit = {
    try {
      avatarUpdateLock.lock()
      if (imageUrl.isEmpty || avatarUrls.getOrElse(identity, "").equals(imageUrl)) {
        return // NOP
      }

      try {
        avatars.put(identity, new Image(imageUrl))
        avatarUrls.put(identity, imageUrl)
      } catch {
        case _: Exception => // NOP
      }
    } finally {
      avatarUpdateLock.unlock()
    }
  }
}
