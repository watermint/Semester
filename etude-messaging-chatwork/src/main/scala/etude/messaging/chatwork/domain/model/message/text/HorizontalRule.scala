package etude.messaging.chatwork.domain.model.message.text

case class HorizontalRule() extends Fragment {
  def render(): String = "[hr]"
}

