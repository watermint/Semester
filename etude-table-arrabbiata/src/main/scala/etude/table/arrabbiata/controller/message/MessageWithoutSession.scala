package etude.table.arrabbiata.controller.message

trait MessageWithoutSession extends Message {
  def perform(): Unit
}
