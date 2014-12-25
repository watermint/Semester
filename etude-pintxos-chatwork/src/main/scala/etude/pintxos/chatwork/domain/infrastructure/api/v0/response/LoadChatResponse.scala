package etude.pintxos.chatwork.domain.infrastructure.api.v0.response

import etude.pintxos.chatwork.domain.model.message.Message
import org.json4s.JValue

case class LoadChatResponse(rawResponse: JValue,
                            chatList: Seq[Message] = Seq(),
                            description: Option[String] = None,
                            publicDescription: Option[String] = None)
  extends ChatWorkResponse