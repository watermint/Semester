package etude.kitchenette.fedelini.provisioning

import etude.messaging.chatwork.domain.model.account.AccountId
import etude.messaging.chatwork.domain.model.room.AccountRoleType

case class RoomRoleMapping(accountId: AccountId,
                           toRoomRole: Option[AccountRoleType],
                           fromRoomRole: Option[AccountRoleType])
