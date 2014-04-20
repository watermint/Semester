package etude.messaging.chatwork.domain.model.message.text

case class Chunk(fragments: Seq[Fragment]) extends Fragment {
  def render(): String = fragments.map(_.render()).mkString
}
