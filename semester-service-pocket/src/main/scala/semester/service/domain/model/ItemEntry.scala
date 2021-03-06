package semester.service.domain.model

import java.net.URL

case class ItemEntry(url: URL,
                     title: Option[String] = None,
                     tags: Seq[String] = Seq.empty,
                     tweetId: Option[String] = None)
