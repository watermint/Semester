package etude.app.arrabbiata.controller.message

trait MessageWithoutSession extends Message {
  def perform(): Unit
}
