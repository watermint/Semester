package etude.chatwork.model

import etude.commons.domain.Identity

class RoomRoleId(val accountId: AccountId,
                 val roomId: RoomId)
  extends Identity[(BigInt, BigInt)] {

  def value: (BigInt, BigInt) = accountId.value -> roomId.value


}
