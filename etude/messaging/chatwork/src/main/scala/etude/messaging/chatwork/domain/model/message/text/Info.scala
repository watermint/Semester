package etude.messaging.chatwork.domain.model.message.text

case class Info(title: Option[String],
                content: Chunk) extends Fragment {
  def render(): String = {
    title match {
      case Some(t) => s"[info][title]$t[/title]${content.render()}[/info]"
      case _ => s"[info]${content.render()}[/info]"
    }
  }
}
