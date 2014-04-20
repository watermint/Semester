package etude.messaging.chatwork.domain.model.message.text

import etude.messaging.chatwork.domain.model.account.AccountId
import java.time.Instant

case class Quote(accountId: AccountId,
                 time: Option[Instant],
                 content: Chunk) extends Fragment {
  def render(): String = {
    time match {
      case Some(t) => s"[qt][qtmeta aid=${accountId.value} time=${t.getEpochSecond}]${content.render()}[/qt]"
      case _ => s"[qt][qtmeta aid=${accountId.value}]${content.render()}[/qt]"
    }
  }
}
