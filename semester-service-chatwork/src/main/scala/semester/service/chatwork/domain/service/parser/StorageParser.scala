package semester.service.chatwork.domain.service.parser

import semester.service.chatwork.domain.service.model.Storage
import org.json4s._

object StorageParser extends ParserBase {
  def parseStorage(json: JValue): Storage = {
    val results: List[Storage] = for {
      JObject(j) <- json
      JField("chat", JString(chat)) <- j
      JField("file", JString(file)) <- j
      JField("total", JInt(total)) <- j
      JField("other", JInt(other)) <- j
    } yield {
      Storage(
        BigInt(chat),
        BigInt(file),
        total,
        other
      )
    }
    results.last
  }
}
