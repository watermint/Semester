package etude.messaging.chatwork.domain.model.message.text

import java.time.Instant

import etude.messaging.chatwork.domain.model.account.AccountId

case class Quote(accountId: AccountId,
                 time: Option[Instant],
                 content: Chunk) extends Fragment with Aggregation {
  def render(): String = {
    time match {
      case Some(t) => s"[qt][qtmeta aid=${accountId.value} time=${t.getEpochSecond}]${content.render()}[/qt]"
      case _ => s"[qt][qtmeta aid=${accountId.value}]${content.render()}[/qt]"
    }
  }

  def fragments(): Seq[Fragment] = content.fragments()
}
