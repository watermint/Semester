package etude.epice.sel

import java.io.File

import etude.epice.sel.auth.oauth.OAuthScope
import etude.epice.sel.client.{OAuthSession, CommonSession, Session, Client}
import etude.epice.sel.policy.execution.Concurrency
import etude.epice.sel.recipe.Recipe
import etude.epice.sel.request.{Multipart, Verb}
import etude.epice.sel.response.ResponseXML

object Design {

  /**
   * service recipe for flickr.
   * - authentication mechanism
   * - REST styles like header, query string, etc.
   * - recipe should know qos/limitation/recovery method for the service.
   */
  case class FlickrRecipe(apiKey: String, apiSecret: String, scope: OAuthScope) extends Recipe {

  }

  def useCase1(): Unit = {
    val recipe: Recipe = FlickrRecipe("<api-key>", "<api-secret>", OAuthScope("write")).withPolicy(Concurrency(2)) // customize policy
    val client: Client = Client.of(recipe)
    client.currentSession() match {
      case session: CommonSession =>
        // restore token from storage

        // acquire requestToken

        // user approval

        // verify + acquire access Token


      case session: OAuthSession =>
        // biz logic

        session.prepareRequest(Verb.GET)
          .withQueryString("method", "flickr.test.echo")
          .execute() map {
          case r: ResponseXML =>
          // do something
          case _ =>
            throw new UnsupportedOperationException()
        }

        val imagePayload = new Multipart()

        imagePayload.withText("title", "Barbecue")
        imagePayload.withText("description", "Summer vacation")
        imagePayload.withText("is_public", "0")
        imagePayload.withText("is_family", "1")
        imagePayload.withText("is_friend", "1")

        session.signer().signature(imagePayload)

        // "photo" should not include for signature
        imagePayload.withBinary("photo", new File("barbecue.jpg"))

        session.prepareRequest(Verb.POST)
          .withPayload(imagePayload)
          .execute() map {
          case r: ResponseXML =>
          // do something
          case _ =>
            throw new UnsupportedOperationException()
        }
    }
  }
}
