package etude.messaging.chatwork.domain.model.account

import etude.domain.core.model.Identity

case class ChatWorkId(value: String)
  extends Identity[String]