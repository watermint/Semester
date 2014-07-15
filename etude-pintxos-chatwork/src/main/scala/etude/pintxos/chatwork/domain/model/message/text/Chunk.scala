package etude.pintxos.chatwork.domain.model.message.text

case class Chunk(chunk: Seq[Fragment]) extends Fragment with Aggregation {
  def render(): String = chunk.map(_.render()).mkString

  def fragments(): Seq[Fragment] = {
    chunk flatMap {
      case c: Aggregation =>
        c.fragments() :+ c
      case c =>
        Seq(c)
    }
  }
}

