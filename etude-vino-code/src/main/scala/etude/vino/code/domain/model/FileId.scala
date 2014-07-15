package etude.vino.code.domain.model

import etude.domain.core.model.Identity

case class FileId(path: String) extends Identity[String] {
  def value: String = path
}
