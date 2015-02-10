package semester.service.chatwork.domain.model.message.text

trait Aggregation {
  def fragments(): Seq[Fragment]
}
