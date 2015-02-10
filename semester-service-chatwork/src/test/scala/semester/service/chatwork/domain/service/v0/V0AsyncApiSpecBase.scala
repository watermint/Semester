package semester.service.chatwork.domain.service.v0

import java.util.Properties

import semester.foundation.undisclosed._
import org.specs2.execute.Result

trait V0AsyncApiSpecBase {
  def getEntityIOContext(prop: Properties): ChatWorkIOContext = {
    (prop.getProperty("organizationId"),
      prop.getProperty("email"),
      prop.getProperty("password")) match {

      case (_, null, _) => throw new IllegalStateException(s"Property 'email' required for test")
      case (_, _, null) => throw new IllegalStateException(s"Property 'password' required for test")
      case (null, username, password) => ChatWorkIOContext(username, password)
      case ("", username, password) => ChatWorkIOContext(username, password)
      case (orgId, username, password) => ChatWorkIOContext(orgId, username, password)
    }
  }

  def withContext(spec: ChatWorkIOContext => Result): Result = {
    undisclosed("semester.service.chatwork.domain.service.ChatWorkIOContext") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
