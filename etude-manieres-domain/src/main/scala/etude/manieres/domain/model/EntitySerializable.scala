package etude.manieres.domain.model

trait EntitySerializable[ID <: Identity[_ <: java.io.Serializable], E <: Entity[ID]]
  extends Serializable {
  this: Entity[ID] =>
}