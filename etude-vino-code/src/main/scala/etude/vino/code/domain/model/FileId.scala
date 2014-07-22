package etude.vino.code.domain.model

import etude.manieres.domain.model.Identity

case class FileId(path: String) extends Identity[String] {
  def value: String = path
}
