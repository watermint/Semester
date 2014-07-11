package etude.app.arrabbiata.state

import java.util.concurrent.atomic.AtomicReference

import etude.adapter.chatwork.domain.model.room.Room

object Rooms {
  val rooms: AtomicReference[Seq[Room]] = new AtomicReference[Seq[Room]]()
}
