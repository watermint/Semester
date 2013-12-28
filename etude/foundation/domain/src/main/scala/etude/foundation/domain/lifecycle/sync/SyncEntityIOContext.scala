package etude.foundation.domain.lifecycle.sync

import etude.foundation.domain.lifecycle.EntityIOContext
import scala.util.Try

trait SyncEntityIOContext extends EntityIOContext[Try]

object SyncEntityIOContext extends SyncEntityIOContext
