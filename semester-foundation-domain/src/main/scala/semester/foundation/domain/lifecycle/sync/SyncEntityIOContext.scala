package semester.foundation.domain.lifecycle.sync

import semester.foundation.domain.lifecycle.EntityIOContext

import scala.util.Try

trait SyncEntityIOContext extends EntityIOContext[Try]

object SyncEntityIOContext extends SyncEntityIOContext
