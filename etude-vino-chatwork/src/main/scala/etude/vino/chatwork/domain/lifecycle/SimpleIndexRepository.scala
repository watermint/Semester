package etude.vino.chatwork.domain.lifecycle

import etude.manieres.domain.model.{Entity, Identity}

trait SimpleIndexRepository[E <: Entity[ID], ID <: Identity[_]] extends Repository[E, ID] {
  val indexName: String

  val typeName: String

  def indexName(entity: E): String = indexName

  def typeName(entity: E): String = typeName

  def get(identity: ID): Option[E] = {
    engine.get(
      indexName,
      typeName,
      toIdentity(identity)
    ) map {
      json =>
        fromJson(json)
    }
  }
}
