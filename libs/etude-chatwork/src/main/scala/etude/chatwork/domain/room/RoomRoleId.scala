package etude.chatwork.domain.room

import etude.ddd.model.Identity
import etude.chatwork.domain.account.AccountId

class RoomRoleId(val accountId: AccountId,
                 val roomId: RoomId)
  extends Identity[(BigInt, BigInt)] {

  def value: (BigInt, BigInt) = accountId.value -> roomId.value
}
