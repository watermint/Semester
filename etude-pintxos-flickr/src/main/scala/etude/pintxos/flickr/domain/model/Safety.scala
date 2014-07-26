package etude.pintxos.flickr.domain.model

trait Safety {
  val value: Int
}

case class SafetySafe() extends Safety {
  val value: Int = 1
}

case class SafetyModerate() extends Safety {
  val value: Int = 2
}

case class SafetyRestricted() extends Safety {
  val value: Int = 3
}