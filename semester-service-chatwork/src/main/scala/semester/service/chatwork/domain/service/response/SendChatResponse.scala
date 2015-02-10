package semester.service.chatwork.domain.service.response

import semester.service.chatwork.domain.model.message.Message
import semester.service.chatwork.domain.service.model.Storage
import semester.service.chatwork.domain.service.request.SendChatRequest
import org.json4s.JValue


case class SendChatResponse(rawResponse: JValue,
                            request: SendChatRequest,
                            storage: Storage,
                            storageLimit: BigInt,
                            messages: Seq[Message])
  extends ChatWorkResponse {
  type Request = SendChatRequest
}
