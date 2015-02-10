package semester.application.vino.service.api

trait Priority {
  val name: String
}

case object PriorityP1 extends Priority {
  val name: String = "Realtime"
}

case object PriorityP2 extends Priority {
  val name: String = "High"
}

case object PriorityP3 extends Priority {
  val name: String = "Normal"
}

case object PriorityP4 extends Priority {
  val name: String = "Low"
}

case object PriorityP5 extends Priority {
  val name: String = "Bottom"
}

object Priority {
  val priorities = Seq(
    PriorityP1,
    PriorityP2,
    PriorityP3,
    PriorityP4,
    PriorityP5
  )
}