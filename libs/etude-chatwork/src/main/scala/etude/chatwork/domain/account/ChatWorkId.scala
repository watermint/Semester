package etude.chatwork.domain.account

import etude.ddd.model.Identity

class ChatWorkId(val id: String) extends Identity[String] {
  def value: String = id
}
