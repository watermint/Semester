package etude.adapter.chatwork.domain.model.message.text

trait Aggregation {
  def fragments(): Seq[Fragment]
}
