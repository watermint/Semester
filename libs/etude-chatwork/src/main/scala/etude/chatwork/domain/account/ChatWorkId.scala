package etude.chatwork.domain.account

import etude.commons.domain.Identity

class ChatWorkId(val id: String)
  extends Identity[String] {

  def value: String = id
}
