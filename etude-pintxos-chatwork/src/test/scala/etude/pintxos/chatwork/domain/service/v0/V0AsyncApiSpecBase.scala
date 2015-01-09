package etude.pintxos.chatwork.domain.service.v0

import java.util.Properties

import etude.epice.undisclosed._
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
    undisclosed("etude.chatwork.infrastructure.api.v0.V0AsyncApi") {
      properties =>
        spec(getEntityIOContext(properties))
    }
  }
}
