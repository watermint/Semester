package etude.manieres.domain.lifecycle.sync

import etude.manieres.domain.lifecycle.EntityIOContext

import scala.util.Try

trait SyncEntityIOContext extends EntityIOContext[Try]

object SyncEntityIOContext extends SyncEntityIOContext
