package semester.readymade.spray

import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import spray.io.ServerSSLEngineProvider

trait SecureConfiguration {
  def keyStoreResource: String = "/kitchenette.jks"
  def keyStorePassword: String = "kitchenette"

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
      /*
       * To use AES 256:
       * @see http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
       *
       * To enable PFS(Perfect Forward Secrecy), use ECDHE or DHE for key exchange
       * @see https://blogs.oracle.com/java-platform-group/entry/java_8_will_use_tls
       */
      engine.setEnabledCipherSuites(Array("TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA"))
      engine.setEnabledProtocols(Array("SSLv3", "TLSv1.2"))
      engine
  }
}
