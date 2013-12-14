package etude.chatwork.infrastructure.elasticsearch

import org.elasticsearch.client.Client

trait Engine {
  val client: Client
}
