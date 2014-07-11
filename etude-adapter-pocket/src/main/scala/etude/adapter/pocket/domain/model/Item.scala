package etude.adapter.pocket.domain.model

import java.net.URL

case class Item(itemId: String,
                normalUrl: URL,
                resolvedId: String,
                resolvedUrl: URL,
                mimeType: String)
