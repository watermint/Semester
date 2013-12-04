package etude.chatwork.infrastructure.cache

import etude.ddd.model.{Entity, Identity}
import scala.collection.mutable
import java.time.Instant
import scala.util.{Failure, Success, Try}

trait CacheQoS[ID <: Identity[_], E <: Entity[ID]] {
  protected val ENTITY_LIST_OPERATION_CACHE_IN_SECONDS = 180

  protected val entityCache = mutable.HashMap[ID, E]()

  protected var lastEntityListOperation: Instant = Instant.EPOCH

  protected def cacheEntity(entity: E): Unit = {
    entityCache.put(entity.identity, entity)
  }

  protected def cachedEntity(identity: ID): Option[E] = {
    entityCache.get(identity)
  }

  protected def entityListOperation(operation: => Try[List[E]]): Try[List[E]] = {
    if (lastEntityListOperation
      .plusSeconds(ENTITY_LIST_OPERATION_CACHE_IN_SECONDS)
      .isAfter(Instant.now)) {
      Success(entityCache.values.toList)
    } else {
      operation match {
        case Failure(f) => Failure(f)
        case Success(list) =>
          list.foreach(cacheEntity)
          Success(list)
      }
    }
  }

  protected def entityResolveOperation(identifier: ID)(operation: => Try[E]): Try[E] = {
    cachedEntity(identifier) match {
      case Some(entity) => Success(entity)
      case _ =>
        operation match {
          case Failure(f) => Failure(f)
          case Success(entity) =>
            cacheEntity(entity)
            Success(entity)
        }
    }
  }
}
