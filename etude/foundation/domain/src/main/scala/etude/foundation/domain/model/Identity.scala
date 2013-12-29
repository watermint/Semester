package etude.foundation.domain.model

trait Identity[+A] {
  def value: A
}

object Identity {
  def apply[A](value: A): Identity[A] = new IdentityImpl(value)

  def unapply[A](v: Identity[A]): Option[A] = Some(v.value)
}

private[model]
class IdentityImpl[A](val value: A)
  extends Identity[A] {

  override def equals(that: Any) = {
    that match {
      case id: Identity[_] => value == id.value
      case _ => false
    }
  }

  override def hashCode = 31 * value.##

  override def toString = s"Identity($value)"
}
