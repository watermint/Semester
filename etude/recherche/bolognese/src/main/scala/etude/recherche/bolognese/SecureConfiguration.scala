package etude.recherche.bolognese

import java.security.{SecureRandom, KeyStore}
import javax.net.ssl.{TrustManagerFactory, KeyManagerFactory, SSLContext}

import spray.io.ServerSSLEngineProvider

trait SecureConfiguration {
  def keyStoreResource: String

  def keyStorePassword: String

  implicit def sslContext: SSLContext = {

    val keyStore = KeyStore.getInstance("jks")
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    val context = SSLContext.getInstance("TLS")

    keyStore.load(getClass.getResourceAsStream(keyStoreResource), keyStorePassword.toCharArray)
    keyManagerFactory.init(keyStore, keyStorePassword.toCharArray)
    trustManagerFactory.init(keyStore)
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    context
  }

  implicit val sslEngine = ServerSSLEngineProvider {
    engine =>
      // TLS_RSA_WITH_AES_256_CBC_SHA requires Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files
      // @see http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html

      engine.setEnabledCipherSuites(Array("TLS_RSA_WITH_AES_128_CBC_SHA"))
      engine.setEnabledProtocols(Array("SSLv3", "TLSv1.2"))
      engine
  }
}
