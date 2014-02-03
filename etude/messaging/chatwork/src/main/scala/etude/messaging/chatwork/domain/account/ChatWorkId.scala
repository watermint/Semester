package etude.messaging.chatwork.domain.account

import etude.foundation.domain.model.Identity

case class ChatWorkId(value: String)
  extends Identity[String]