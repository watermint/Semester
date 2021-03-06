package semester.service.chatwork.domain.model.account

import java.net.URI

import semester.foundation.domain.model.Entity

/**
 * Account in ChatWork.
 * @see http://developer.chatwork.com/ja/endpoint_contacts.html
 * @see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms
 */
class Account(val accountId: AccountId,
              val name: Option[String] = None,
              val chatWorkId: Option[ChatWorkId] = None,
              val organization: Option[Organization] = None,
              val department: Option[String] = None,
              val avatarImage: Option[URI] = None)
  extends Entity[AccountId] {

  val identity: AccountId = accountId
}
