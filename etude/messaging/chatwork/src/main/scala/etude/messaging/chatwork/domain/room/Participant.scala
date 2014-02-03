package etude.messaging.chatwork.domain.room

import etude.messaging.chatwork.domain.account.AccountId
import etude.foundation.domain.model.Entity

class Participant(val roomId: RoomId,
                  val admin: Seq[AccountId],
                  val member: Seq[AccountId],
                  val readonly: Seq[AccountId])
  extends Entity[RoomId] {

  val identity: RoomId = roomId

  def copy(admin: Seq[AccountId] = this.admin,
           member: Seq[AccountId] = this.member,
           readonly: Seq[AccountId] = this.readonly): Participant = {
    new Participant(
      roomId = roomId,
      admin = admin,
      member = member,
      readonly = readonly
    )
  }
}
