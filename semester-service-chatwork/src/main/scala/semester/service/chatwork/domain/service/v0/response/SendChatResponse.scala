package semester.service.chatwork.domain.service.v0.response

import semester.service.chatwork.domain.model.message.Message
import semester.service.chatwork.domain.service.v0.model.Storage
import semester.service.chatwork.domain.service.v0.request.SendChatRequest
import org.json4s.JValue


case class SendChatResponse(rawResponse: JValue,
                            request: SendChatRequest,
                            storage: Storage,
                            storageLimit: BigInt,
                            messages: Seq[Message])
  extends ChatWorkResponse {
  type Request = SendChatRequest
}
