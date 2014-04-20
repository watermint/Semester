package etude.messaging.chatwork.domain.model.message.text

trait Aggregation {
  def fragments(): Seq[Fragment]
}
