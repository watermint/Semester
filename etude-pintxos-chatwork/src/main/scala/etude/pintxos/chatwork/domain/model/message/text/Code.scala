package etude.pintxos.chatwork.domain.model.message.text

case class Code(code: String) extends Fragment {
  def render(): String = s"[code]$code[/code]"
}
