package etude.pintxos.chatwork.domain.service.v0.response

import org.json4s.JValue

case class ReadResponse(rawResponse: JValue,
                        readNum: BigInt,
                        mentionNum: BigInt)
  extends ChatWorkResponse
