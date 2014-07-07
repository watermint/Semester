package etude.app.bolognese

import java.security.{SecureRandom, KeyStore}
import javax.net.ssl.{TrustManagerFactory, KeyManagerFactory, SSLContext}

import spray.io.ServerSSLEngineProvider

trait SecureConfiguration {
  implicit def sslContext: SSLContext = {
    val keyStoreResource = "/bolognese.jks"
    val password = "semester"

    val keyStore = KeyStore.getInstance("jks")
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    val context = SSLContext.getInstance("TLS")

    keyStore.load(getClass.getResourceAsStream(keyStoreResource), password.toCharArray)
    keyManagerFactory.init(keyStore, password.toCharArray)
    trustManagerFactory.init(keyStore)
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    context
  }

  implicit val sslEngine = ServerSSLEngineProvider {
    engine =>
      // TLS_RSA_WITH_AES_256_CBC_SHA
      engine.setEnabledCipherSuites(Array("TLS_RSA_WITH_AES_128_CBC_SHA"))
      engine.setEnabledProtocols(Array("SSLv3", "TLSv1"))
      engine
  }
}
