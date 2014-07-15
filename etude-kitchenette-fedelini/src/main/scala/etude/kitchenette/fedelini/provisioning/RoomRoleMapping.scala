package etude.kitchenette.fedelini.provisioning

import etude.pintxos.chatwork.domain.model.account.AccountId
import etude.pintxos.chatwork.domain.model.room.AccountRoleType

case class RoomRoleMapping(accountId: AccountId,
                           toRoomRole: Option[AccountRoleType],
                           fromRoomRole: Option[AccountRoleType])
