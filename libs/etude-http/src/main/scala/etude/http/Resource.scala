package etude.http

/**
 *
 */
case class Resource(uri: URIContainer) {
  def get: Either[Exception, Response] =  Client().get(uri)

  def post(formData: List[Pair[String, String]] = List()): Either[Exception, Response] = Client().post(uri, formData)
}
