package etude.messaging.chatwork.domain.infrastructure.api.v0.auth

import java.net.URI
import scala.util.Try
import scala.xml.Node
import etude.foundation.html.Normalizer
import etude.foundation.http._
import etude.messaging.chatwork.domain.infrastructure.api.v0.V0UnknownChatworkProtocolException

class Exaggerated extends Auth {

  case class ExaggeratedForm(uri: URI,
                             action: String,
                             userName: String,
                             password: String,
                             viewState: String,
                             eventValidation: String) {
    val params = Map(
      "ctl00$ContentPlaceHolder1$UsernameTextBox" -> userName,
      "ctl00$ContentPlaceHolder1$PasswordTextBox" -> password,
      "ctl00$ContentPlaceHolder1$SubmitButton" -> "Sign In",
      "__EVENTVALIDATION" -> eventValidation,
      "__VIEWSTATE" -> viewState,
      "__db" -> "14"
    )
  }

  case class ExaggeratedAssertion(action: String,
                                  assertion: String)

  def acceptable(context: AuthContext): Boolean = {
    context.startPageContent.contains("__EVENTVALIDATION") &&
      context.redirectedUri.isDefined
  }

  def acquireToken(context: AuthContext): Try[AuthToken] = {
    parseStartPage(context) flatMap {
      form =>
        submitForm(form, context) flatMap {
          assertion =>
            submitAssertion(assertion, context)
        }
    }
  }

  private def submitAssertion(assertion: ExaggeratedAssertion, context: AuthContext): Try[AuthToken] = {
    val uri = new URI(assertion.action)

    context.client.post(
      uri,
      List("SAMLResponse" -> assertion.assertion)
    ) map {
      response =>
        parsePage(response.contentAsString) match {
          case Some(token) => token
          case _ => throw new V0UnknownChatworkProtocolException("Authentication failed")
        }
    }
  }

  private def submitForm(form: ExaggeratedForm, context: AuthContext): Try[ExaggeratedAssertion] = {
    val formUri = new URI(s"${form.uri.getScheme}://${form.uri.getHost}${form.action}")

    context.client.post(
      formUri,
      form.params.toList
    ) flatMap {
      response =>
        parseSubmitResponse(response)
    }
  }

  private def parseSubmitResponse(response: Response): Try[ExaggeratedAssertion] = {
    Normalizer.html(response.contentAsString) map {
      node =>
        parseSubmitResponse(node)
    }
  }

  private def parseSubmitResponse(source: Node): ExaggeratedAssertion = {
    ExaggeratedAssertion(
      action = (source \\ "form" \ "@action").toString(),
      assertion = ((source \\ "input")(0) \ "@value").toString()
    )
  }

  private def parseStartPage(context: AuthContext): Try[ExaggeratedForm] = {
    Normalizer.html(context.startPageContent) map {
      node =>
        parseStartPage(node, context)
    }
  }

  private def parseStartPage(source: Node, context: AuthContext): ExaggeratedForm = {
    val inputs = (source \\ "input").map {
      input =>
        (input \ "@id").toString -> (input \ "@value").toString
    }.toMap

    ExaggeratedForm(
      uri = context.redirectedUri.get,
      action = (source \\ "form" \ "@action")(0).toString(),
      userName = context.username,
      password = context.password,
      viewState = inputs.getOrElse("__VIEWSTATE", ""),
      eventValidation = inputs.getOrElse("__EVENTVALIDATION", "")
    )
  }
}