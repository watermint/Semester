package etude.domain.core.lifecycle.sync

import etude.domain.core.lifecycle.EntityIOContext

import scala.util.Try

trait SyncEntityIOContext extends EntityIOContext[Try]

object SyncEntityIOContext extends SyncEntityIOContext
