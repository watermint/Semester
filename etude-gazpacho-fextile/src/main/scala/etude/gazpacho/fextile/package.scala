package etude.gazpacho

import javafx.event.{Event, EventHandler}

package object fextile {
  def event[T <: Event](f: T => Unit): EventHandler[T] = new EventHandler[T] {
    def handle(e: T) = f(e)
  }

  val xs1 = GridSpanExtraSmall(1)
  val xs2 = GridSpanExtraSmall(2)
  val xs3 = GridSpanExtraSmall(3)
  val xs4 = GridSpanExtraSmall(4)
  val xs5 = GridSpanExtraSmall(5)
  val xs6 = GridSpanExtraSmall(6)
  val xs7 = GridSpanExtraSmall(7)
  val xs8 = GridSpanExtraSmall(8)
  val xs9 = GridSpanExtraSmall(9)
  val xs10 = GridSpanExtraSmall(10)
  val xs11 = GridSpanExtraSmall(11)
  val xs12 = GridSpanExtraSmall(12)

  val sm1 = GridSpanSmall(1)
  val sm2 = GridSpanSmall(2)
  val sm3 = GridSpanSmall(3)
  val sm4 = GridSpanSmall(4)
  val sm5 = GridSpanSmall(5)
  val sm6 = GridSpanSmall(6)
  val sm7 = GridSpanSmall(7)
  val sm8 = GridSpanSmall(8)
  val sm9 = GridSpanSmall(9)
  val sm10 = GridSpanSmall(10)
  val sm11 = GridSpanSmall(11)
  val sm12 = GridSpanSmall(12)

  val md1 = GridSpanMedium(1)
  val md2 = GridSpanMedium(2)
  val md3 = GridSpanMedium(3)
  val md4 = GridSpanMedium(4)
  val md5 = GridSpanMedium(5)
  val md6 = GridSpanMedium(6)
  val md7 = GridSpanMedium(7)
  val md8 = GridSpanMedium(8)
  val md9 = GridSpanMedium(9)
  val md10 = GridSpanMedium(10)
  val md11 = GridSpanMedium(11)
  val md12 = GridSpanMedium(12)

  val lg1 = GridSpanLarge(1)
  val lg2 = GridSpanLarge(2)
  val lg3 = GridSpanLarge(3)
  val lg4 = GridSpanLarge(4)
  val lg5 = GridSpanLarge(5)
  val lg6 = GridSpanLarge(6)
  val lg7 = GridSpanLarge(7)
  val lg8 = GridSpanLarge(8)
  val lg9 = GridSpanLarge(9)
  val lg10 = GridSpanLarge(10)
  val lg11 = GridSpanLarge(11)
  val lg12 = GridSpanLarge(12)
}
