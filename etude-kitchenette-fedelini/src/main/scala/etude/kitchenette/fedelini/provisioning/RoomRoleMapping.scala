package etude.kitchenette.fedelini.provisioning

import etude.adapter.chatwork.domain.model.account.AccountId
import etude.adapter.chatwork.domain.model.room.AccountRoleType

case class RoomRoleMapping(accountId: AccountId,
                           toRoomRole: Option[AccountRoleType],
                           fromRoomRole: Option[AccountRoleType])
