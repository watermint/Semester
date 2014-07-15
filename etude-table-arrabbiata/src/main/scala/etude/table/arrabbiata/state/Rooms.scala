package etude.table.arrabbiata.state

import java.util.concurrent.atomic.AtomicReference

import etude.pintxos.chatwork.domain.model.room.Room

object Rooms {
  val rooms: AtomicReference[Seq[Room]] = new AtomicReference[Seq[Room]]()
}
