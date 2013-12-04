package etude.chatwork.model

import etude.ddd.model.Identity

class ChatWorkId(id: String) extends Identity[String] {
  def value: String = id
}
