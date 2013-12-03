package etude.chatwork.v1

import java.net.URI

/**
 * Account in ChatWork.
 * @see http://developer.chatwork.com/ja/endpoint_contacts.html
 * @see http://developer.chatwork.com/ja/endpoint_rooms.html#GET-rooms
 */
case class Account(accountId: AccountId,
                   name: String,
                   chatWorkId: Option[ChatWorkId],
                   organization: Option[Organization],
                   department: Option[String],
                   avatarImage: URI)
  extends Entity[AccountId]