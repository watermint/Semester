package etude.epice.sel

import java.awt.Desktop
import java.io.File
import java.net.URI
import java.util.Scanner

import etude.epice.sel.auth.oauth.{OAuthApp, OAuthScope, OAuthVerifier}
import etude.epice.sel.client.{Client, OAuthSession}
import etude.epice.sel.recipe.Recipe
import etude.epice.sel.request.{Request, Multipart, Verb}
import etude.epice.sel.response.{Response, ResponseXML}

object Design {

  trait OAuthRecipe extends Recipe {
    val workflow: (URI) => OAuthVerifier

    val appInfo: OAuthApp
  }

  /**
   * service recipe for flickr.
   * - authentication mechanism
   * - REST styles like header, query string, etc.
   * - recipe should know qos/limitation/recovery method for the service.
   */
  case class FlickrRecipe(appInfo: OAuthApp,
                          workflow: (URI) => OAuthVerifier) extends OAuthRecipe {

  }

  def useCase1(): Unit = {
    val client: Client = Client()
    val app: OAuthApp = OAuthApp("<api-key>", "<api-secret>", OAuthScope("write"))
    val recipe: Recipe = FlickrRecipe(app, {
      authUri =>
        Desktop.getDesktop.browse(authUri)
        OAuthVerifier(new Scanner(System.in).nextLine())
    })

    val session = recipe.ofSession[OAuthSession[Request[Response], Response]](client)

    session.prepareRequest(Verb.GET)
      .withQueryString("method", "flickr.test.echo")
      .execute()

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
