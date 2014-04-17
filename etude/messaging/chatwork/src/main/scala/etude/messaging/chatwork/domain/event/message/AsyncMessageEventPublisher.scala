package etude.messaging.chatwork.domain.event.message

import etude.foundation.domain.event.async.{AsyncEntityEventSubscriber, AsyncEntityEventPublisher}
import etude.messaging.chatwork.domain.model.message.{MessageId, Message}

trait AsyncMessageEventPublisher
  extends AsyncEntityEventPublisher[MessageId, Message] {

  type Publisher = AsyncMessageEventPublisher

  type Subscriber = AsyncEntityEventSubscriber[MessageId, Message]
}
