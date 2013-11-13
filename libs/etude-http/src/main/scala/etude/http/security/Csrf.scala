package etude.http.security

import java.util.UUID
import org.apache.commons.codec.digest.DigestUtils

case class Csrf(userSeed: String) {
  lazy val token = DigestUtils.sha256Hex(userSeed + Csrf.systemSeed)

  def verify(otherToken: String): Boolean = token.equals(otherToken)
}

object Csrf {
  lazy val systemSeed: String = UUID.randomUUID().toString
}
