package etude.commons.domain

/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * @see https://github.com/sisioh/scala-dddbase/blob/develop/scala-dddbase-core/src/main/scala/org/sisioh/dddbase/core/model/Identity.scala
 */

trait Identity[+A] {
  def value: A
}

/* Note: These trait/class are not used in this Semester now.

trait OrderedIdentity[A, ID <: Identity[A]]
  extends Identity[A] with Ordered[ID]

abstract class AbstractOrderedIdentity[A <% Ordered[A], ID <: Identity[A]]
  extends OrderedIdentity[A, ID] {

  def compare(that: ID): Int = {
    value compare that.value
  }

}

case class EmptyIdentityException() extends Exception

class EmptyIdentity extends Identity[Nothing] {

  def value = throw EmptyIdentityException()

  override def equals(obj: Any): Boolean = obj match {
    case that: EmptyIdentity => this eq that
    case _ => false
  }

  override def hashCode(): Int = 31 * 1

  override def toString = "EmptyIdentity"
}

object EmptyIdentity extends EmptyIdentity

private[ddd]
class IdentityImpl[A](val value: A)
  extends Identity[A] {

  override def equals(obj: Any) = obj match {
    case that: EmptyIdentity => false
    case that: Identity[_] =>
      value == that.value
    case _ => false
  }

  override def hashCode = 31 * value.##

  override def toString = s"Identity($value)"

}

object Identity {
  def apply[A](value: A): Identity[A] = new IdentityImpl(value)

  def empty[A]: Identity[A] = EmptyIdentity

  def unapply[A](v: Identity[A]): Option[A] = Some(v.value)
}
*/