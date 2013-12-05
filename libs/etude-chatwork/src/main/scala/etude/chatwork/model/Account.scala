package etude.chatwork.model

import java.net.URI
import etude.commons.domain.Entity

/**
 * Account in ChatWork.
 * @see http://developer.chatwork.com/ja/endpoint_contacts.html
 * @see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms
 */
class Account(val accountId: AccountId,
              val name: String,
              val chatWorkId: Option[ChatWorkId],
              val organization: Option[Organization],
              val department: Option[String],
              val avatarImage: URI)
  extends Entity[AccountId] {

  val identity: AccountId = accountId
}
