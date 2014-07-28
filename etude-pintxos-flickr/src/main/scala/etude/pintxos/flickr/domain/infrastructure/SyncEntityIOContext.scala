package etude.pintxos.flickr.domain.infrastructure

import java.util.concurrent.atomic.AtomicReference

import etude.manieres.domain.lifecycle.EntityIOContext
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api.FlickrApi
import org.scribe.model.Token
import org.scribe.oauth.OAuthService

import scala.util.Try

case class SyncEntityIOContext() extends EntityIOContext[Try] {
  /**
   * @see https://www.flickr.com/services/apps/72157645549228870/
   */
  val apiKey: String = "d032ca2c2dae5668fdf05e139e75a57e"

  val apiSecret: String = "bc978629a7bfcc6f"

  val requestToken: AtomicReference[Token] = new AtomicReference[Token]()

  val accessToken: AtomicReference[Token] = new AtomicReference[Token]()

  val service: OAuthService = {
    new ServiceBuilder()
      .provider(classOf[FlickrApi])
      .apiKey(apiKey)
      .apiSecret(apiSecret)
      .build()
  }
}
