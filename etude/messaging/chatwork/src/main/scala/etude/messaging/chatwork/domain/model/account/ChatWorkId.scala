package etude.messaging.chatwork.domain.model.account

import etude.foundation.domain.model.Identity

case class ChatWorkId(value: String)
  extends Identity[String]