package etude.manieres.domain.model

trait Entity[ID <: Identity[_]] {
  val identity: ID

  override final def hashCode: Int = 31 * identity.##

  override final def equals(that: Any): Boolean = that match {
    case that: Entity[_] => identity == that.identity
    case _ => false
  }
}
