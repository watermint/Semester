package etude.foundation.domain.model

trait EntityCloneable[ID <: Identity[_], E <: Entity[ID]]
  extends Cloneable {
  this: Entity[ID] =>

  override def clone: E = super.clone.asInstanceOf[E]
}
