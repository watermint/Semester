package etude.app.arrabbiata.controller.message.room

import etude.app.arrabbiata.controller.message.MessageWithSession
import etude.app.arrabbiata.state.Session
import etude.kitchenette.fedelini.provisioning.{AccountProvisioning, ProvisioningPolicy}
import etude.messaging.chatwork.domain.model.room.Room

case class DoMergeRoom(base: Room, target: Room) extends MessageWithSession {
  def perform(session: Session): Unit = {
    val provisioning = new AccountProvisioning
    implicit val execContext = session.ioContext.executor
    implicit val ioContext = session.ioContext

    provisioning.merge(target.roomId, base.roomId, ProvisioningPolicy.toRoleThenFromRole)
  }
}
