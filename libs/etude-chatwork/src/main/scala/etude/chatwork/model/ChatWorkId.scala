package etude.chatwork.model

import etude.commons.domain.Identity

class ChatWorkId(id: String) extends Identity[String] {
  def value: String = id
}
