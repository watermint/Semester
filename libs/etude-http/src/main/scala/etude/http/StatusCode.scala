package etude.http


trait StatusCode {
  val code: Int
  val label: String
}

object StatusCode {
  def apply(code: Int): StatusCode = {
    code match {
      case 100 => StatusContinue()
      case 101 => StatusSwitchingProtocols()
      case c if c / 100 == 1 => StatusExtendedInformational(code)
      case 200 => StatusOK()
      case 201 => StatusCreated()
      case 202 => StatusAccepted()
      case 203 => StatusNonAuthoritativeInformation()
      case 204 => StatusNoContent()
      case 205 => StatusResetContent()
      case 206 => StatusPartialContent()
      case c if c / 100 == 2 => StatusExtendedSuccessful(code)
      case 300 => StatusMultipleChoices()
      case 301 => StatusMovedPermanently()
      case 302 => StatusFound()
      case 303 => StatusSeeOther()
      case 304 => StatusNotModified()
      case 305 => StatusUseProxy()
      case 307 => StatusTemporaryRedirect()
      case c if c / 100 == 3 => StatusExtendedRedirection(code)
      case 400 => StatusBadRequest()
      case 401 => StatusUnauthorized()
      case 402 => StatusPaymentRequired()
      case 403 => StatusForbidden()
      case 404 => StatusNotFound()
      case 405 => StatusMethodNotAllowed()
      case 406 => StatusNotAcceptable()
      case 407 => StatusProxyAuthenticationRequired()
      case 408 => StatusRequestTimeout()
      case 409 => StatusConflict()
      case 410 => StatusGone()
      case 411 => StatusLengthRequired()
      case 412 => StatusPreconditionFailed()
      case 413 => StatusRequestEntityTooLarge()
      case 414 => StatusRequestURITooLong()
      case 415 => StatusUnsupportedMediaType()
      case 416 => StatusRequestedRangeNotSatisfiable()
      case 417 => StatusExpectationFailed()
      case c if c / 100 == 4 => StatusExtendedClientError(code)
      case 500 => StatusInternalServerError()
      case 501 => StatusNotImplemented()
      case 502 => StatusBadGateway()
      case 503 => StatusServiceUnavailable()
      case 504 => StatusGatewayTimeout()
      case 505 => StatusHTTPVersionNotSupported()
      case c if c / 100 == 5 => StatusExtendedServerError(code)
      case _ => StatusUnknownCode(code)
    }
  }
}

trait StatusExtendedCode extends StatusCode {
  val label: String = "Extended Code"
}

case class StatusUnknownCode(code: Int) extends StatusCode {
  val label: String = "Unknown"
}

/*
 * RFC2616 - 1xx Informational
 */

trait StatusInformational extends StatusCode

case class StatusExtendedInformational(code: Int) extends StatusCode with StatusExtendedCode

case class StatusContinue() extends StatusInformational {
  val code: Int = 100
  val label: String = "Continue"
}

case class StatusSwitchingProtocols() extends StatusInformational {
  val code: Int = 101
  val label: String = "Switching Protocols"
}

/*
 * RFC2616 - 2xx Successful
 */

trait StatusSuccessful extends StatusCode

case class StatusExtendedSuccessful(code: Int) extends StatusCode with StatusExtendedCode

case class StatusOK() extends StatusSuccessful {
  val code: Int = 200
  val label: String = "OK"
}

case class StatusCreated() extends StatusSuccessful {
  val code: Int = 201
  val label: String = "Created"
}

case class StatusAccepted() extends StatusSuccessful {
  val code: Int = 202
  val label: String = "Accepted"
}

case class StatusNonAuthoritativeInformation() extends StatusSuccessful {
  val code: Int = 203
  val label: String = "Non-Authoritative Information"
}

case class StatusNoContent() extends StatusSuccessful {
  val code: Int = 204
  val label: String = "No Content"
}

case class StatusResetContent() extends StatusSuccessful {
  val code: Int = 205
  val label: String = "Reset Content"
}

case class StatusPartialContent() extends StatusSuccessful {
  val code: Int = 206
  val label: String = "Partial Content"
}

/*
 * RFC2616 - 3xx Redirection
 */

trait StatusRedirection extends StatusCode

case class StatusExtendedRedirection(code: Int) extends StatusCode with StatusExtendedCode

case class StatusMultipleChoices() extends StatusRedirection {
  val code: Int = 300
  val label: String = "Multiple Choices"
}

case class StatusMovedPermanently() extends StatusRedirection {
  val code: Int = 301
  val label: String = "Moved Permanently"
}

case class StatusFound() extends StatusRedirection {
  val code: Int = 302
  val label: String = "Found"
}

case class StatusSeeOther() extends StatusRedirection {
  val code: Int = 303
  val label: String = "See Other"
}

case class StatusNotModified() extends StatusRedirection {
  val code: Int = 304
  val label: String = "Not Modified"
}

case class StatusUseProxy() extends StatusRedirection {
  val code: Int = 305
  val label: String = "Use Proxy"
}

case class StatusTemporaryRedirect() extends StatusRedirection {
  val code: Int = 307
  val label: String = "Temporary Redirect"
}


/*
 * RFC2616 - 4xx Client Error
 */

trait StatusClientError extends StatusCode

case class StatusExtendedClientError(code: Int) extends StatusCode with StatusExtendedCode

case class StatusBadRequest() extends StatusClientError {
  val code: Int = 400
  val label: String = "Bad Request"
}

case class StatusUnauthorized() extends StatusClientError {
  val code: Int = 401
  val label: String = "Unauthorized"
}

case class StatusPaymentRequired() extends StatusClientError {
  val code: Int = 402
  val label: String = "Payment Required"
}

case class StatusForbidden() extends StatusClientError {
  val code: Int = 403
  val label: String = "Forbidden"
}

case class StatusNotFound() extends StatusClientError {
  val code: Int = 404
  val label: String = "Not Found"
}

case class StatusMethodNotAllowed() extends StatusClientError {
  val code: Int = 405
  val label: String = "Method Not Allowed"
}

case class StatusNotAcceptable() extends StatusClientError {
  val code: Int = 406
  val label: String = "Not Acceptable"
}

case class StatusProxyAuthenticationRequired() extends StatusClientError {
  val code: Int = 407
  val label: String = "Proxy Authentication Required"
}

case class StatusRequestTimeout() extends StatusClientError {
  val code: Int = 408
  val label: String = "Request Timeout"
}

case class StatusConflict() extends StatusClientError {
  val code: Int = 409
  val label: String = "Conflict"
}

case class StatusGone() extends StatusClientError {
  val code: Int = 410
  val label: String = "Gone"
}

case class StatusLengthRequired() extends StatusClientError {
  val code: Int = 411
  val label: String = "Length Required"
}

case class StatusPreconditionFailed() extends StatusClientError {
  val code: Int = 412
  val label: String = "Precondition Failed"
}

case class StatusRequestEntityTooLarge() extends StatusClientError {
  val code: Int = 413
  val label: String = "Request Entity Too Large"
}

case class StatusRequestURITooLong() extends StatusClientError {
  val code: Int = 414
  val label: String = "Request-URI Too Long"
}

case class StatusUnsupportedMediaType() extends StatusClientError {
  val code: Int = 415
  val label: String = "Unsupported Media Type"
}

case class StatusRequestedRangeNotSatisfiable() extends StatusClientError {
  val code: Int = 416
  val label: String = "Requested Range Not Satisfiable"
}

case class StatusExpectationFailed() extends StatusClientError {
  val code: Int = 417
  val label: String = "Expectation Failed"
}


/*
 * RFC2616 - 5xx Server Error
 */

trait StatusServerError extends StatusCode

case class StatusExtendedServerError(code: Int) extends StatusCode with StatusExtendedCode

case class StatusInternalServerError() extends StatusServerError {
  val code: Int = 500
  val label: String = "Internal Server Error"
}

case class StatusNotImplemented() extends StatusServerError {
  val code: Int = 501
  val label: String = "Not Implemented"
}

case class StatusBadGateway() extends StatusServerError {
  val code: Int = 502
  val label: String = "Bad Gateway"
}

case class StatusServiceUnavailable() extends StatusServerError {
  val code: Int = 503
  val label: String = "Service Unavailable"
}

case class StatusGatewayTimeout() extends StatusServerError {
  val code: Int = 504
  val label: String = "Gateway Timeout"
}

case class StatusHTTPVersionNotSupported() extends StatusServerError {
  val code: Int = 505
  val label: String = "HTTP Version Not Supported"
}