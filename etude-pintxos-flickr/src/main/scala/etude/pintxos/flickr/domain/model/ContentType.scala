package etude.pintxos.flickr.domain.model

trait ContentType {
  val value: Int
}

case class ContentTypePhoto() extends ContentType {
  val value: Int = 1
}

case class ContentTypeScreenshot() extends ContentType {
  val value: Int = 2
}

case class ContentTypeOther() extends ContentType {
  val value: Int = 3
}