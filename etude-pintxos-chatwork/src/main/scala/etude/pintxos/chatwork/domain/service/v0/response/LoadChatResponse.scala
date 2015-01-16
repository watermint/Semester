package etude.pintxos.chatwork.domain.service.v0.response

import etude.pintxos.chatwork.domain.model.message.Message
import etude.pintxos.chatwork.domain.service.v0.request.LoadChatRequest
import org.json4s.JValue

case class LoadChatResponse(rawResponse: JValue,
                            request: LoadChatRequest,
                            chatList: Seq[Message] = Seq(),
                            description: Option[String] = None,
                            publicDescription: Option[String] = None)
  extends ChatWorkResponse {
  type Request = LoadChatRequest
}