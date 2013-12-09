package etude.fextile

trait GridSpan extends DeviceSize {
  val span: Int
  val offset: Option[Int]

  if (!(1 to 12).contains(span)) {
    throw new IllegalArgumentException(s"span should be in range 1 to 12. given span is ${span}")
  }
  if (!(1 to 12).contains(offset.getOrElse(1))) {
    throw new IllegalArgumentException(s"offset should be in range 1 to 12: given offset is ${offset}")
  }
}

case class GridSpanExtraSmall(span: Int = 1, offset: Option[Int] = None) extends GridSpan with DeviceSizeExtraSmall

case class GridSpanSmall(span: Int = 1, offset: Option[Int] = None) extends GridSpan with DeviceSizeSmall

case class GridSpanMedium(span: Int = 1, offset: Option[Int] = None) extends GridSpan with DeviceSizeMedium

case class GridSpanLarge(span: Int = 1, offset: Option[Int] = None) extends GridSpan with DeviceSizeLarge
