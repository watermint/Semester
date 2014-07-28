package etude.pintxos.flickr.domain.infrastructure

import java.io.{ByteArrayOutputStream, InputStream}
import java.nio.charset.StandardCharsets

import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.scribe.model.{OAuthRequest, Token, Verb, Verifier}

import scala.util.{Failure, Success, Try}

object SyncApi {
  val endpoint = "https://api.flickr.com/services/rest/"

  def requestTokenUrl(context: SyncEntityIOContext,
                      permission: AuthPermission = AuthPermission.READ): Try[String] = {
    val requestToken = context.service.getRequestToken
    context.requestToken.set(requestToken)
    Success(
      context.service.getAuthorizationUrl(requestToken) + permission.param
    )
  }

  def verifyRequestToken(verificationCode: String,
                         context: SyncEntityIOContext): Try[Token] = {
    val verifier = new Verifier(verificationCode)
    val accessToken = context.service.getAccessToken(context.requestToken.get, verifier)
    context.accessToken.set(accessToken)
    Success(accessToken)
  }

  def call(verb: Verb = Verb.GET,
           method: String,
           params: Map[String, String] = Map(),
           context: SyncEntityIOContext): Try[String] = {
    context.accessToken.get() match {
      case null => Failure(new IllegalStateException("No Access Token"))
      case token =>
        val request = new OAuthRequest(verb, endpoint)
        request.addQuerystringParameter("method", method)
        params.foreach(p => request.addQuerystringParameter(p._1, p._2))
        context.service.signRequest(token, request)
        val response = request.send()
        Success(response.getBody)
    }
  }

  def upload(params: Map[String, String] = Map(),
             photo: InputStream,
             filename: String,
             context: SyncEntityIOContext): Try[String] = {
    context.accessToken.get() match {
      case null => Failure(new IllegalStateException("No Access Token"))
      case token =>
        val request = new OAuthRequest(Verb.POST, "https://up.flickr.com/services/upload/")
        params.foreach(p => request.addQuerystringParameter(p._1, p._2))

        val payload = new ByteArrayOutputStream()
        val multiPart = MultipartEntityBuilder.create()
        multiPart.setCharset(StandardCharsets.UTF_8)

        multiPart.addBinaryBody("photo", photo, ContentType.create("image/jpeg"), filename)
        val entity = multiPart.build()

        entity.writeTo(payload)
        request.addPayload(payload.toByteArray)
        request.addHeader(entity.getContentType.getName, entity.getContentType.getValue)

        context.service.signRequest(token, request)

        val response = request.send()
        Success(response.getBody)
    }
  }
}
