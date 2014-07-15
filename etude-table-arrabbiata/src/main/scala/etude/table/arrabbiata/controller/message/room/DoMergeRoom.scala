package etude.table.arrabbiata.controller.message.room

import etude.table.arrabbiata.controller.message.MessageWithSession
import etude.table.arrabbiata.state.Session
import etude.vino.chatwork.provisioning.{AccountProvisioning, ProvisioningPolicy}
import etude.pintxos.chatwork.domain.model.room.Room

case class DoMergeRoom(base: Room, target: Room) extends MessageWithSession {
  def perform(session: Session): Unit = {
    val provisioning = new AccountProvisioning
    implicit val execContext = session.ioContext.executor
    implicit val ioContext = session.ioContext

    provisioning.merge(target.roomId, base.roomId, ProvisioningPolicy.toRoleThenFromRole)
  }
}
