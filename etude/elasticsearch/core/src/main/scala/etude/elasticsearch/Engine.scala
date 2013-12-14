package etude.elasticsearch

import org.elasticsearch.client.Client

trait Engine {
  val client: Client
}
