package semester.service.chatwork.domain.service

import semester.foundation.http.Client

import scala.language.higherKinds

trait ChatWorkEntityIO {

  val contextAccessWaitInMillis = 500
  val updateDelayInMills = 10000

  protected def getOrganizationId(context: ChatWorkIOContext): Option[String] = {
    context.organizationId
  }

  protected def getUsername(context: ChatWorkIOContext): String = {
    context.username
  }

  protected def getPassword(context: ChatWorkIOContext): String = {
    context.password
  }

  protected def getClient(context: ChatWorkIOContext): Client = {
    context.client
  }

  protected def getAccessToken(context: ChatWorkIOContext): Option[String] = {
    context.accessToken.get(contextAccessWaitInMillis)
  }

  protected def getMyId(context: ChatWorkIOContext): Option[String] = {
    context.myId.get(contextAccessWaitInMillis)
  }

  protected def getLastId(context: ChatWorkIOContext): Option[String] = {
    context.lastId.get()
  }

  protected def setAccessToken(accessToken: String, context: ChatWorkIOContext): Unit = {
    context.accessToken.put(accessToken)
  }

  protected def setMyId(myId: String, context: ChatWorkIOContext): Unit = {
    context.myId.put(myId)
  }

  protected def setLastId(lastId: String, context: ChatWorkIOContext): Unit = {

    context.lastId.set(lastId)
  }

  protected def clearToken(context: ChatWorkIOContext): Unit = {
    try {
      context.accessToken.take(contextAccessWaitInMillis)
    } catch {
      case _: java.util.NoSuchElementException => // ignore
      case _: Throwable => // ignore
    }
  }

  protected def hasToken(context: ChatWorkIOContext): Boolean = {
    context.accessToken.isSet
  }
}
