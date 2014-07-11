package etude.adapter.chatwork.domain.model.message.text

case class PlainText(text: String) extends Fragment {
  def render(): String = text
}
