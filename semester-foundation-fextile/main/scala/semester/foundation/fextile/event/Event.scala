package semester.foundation.fextile.event

trait Event {
  val source: EventSource

  def enqueue(): Unit = {
    source.currentActor ! (source, this)
  }
}
