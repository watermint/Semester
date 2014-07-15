package etude.gazpacho.logging

import org.slf4j.bridge.SLF4JBridgeHandler

object LoggerFactory {
  SLF4JBridgeHandler.removeHandlersForRootLogger()
  SLF4JBridgeHandler.install()

  def getLogger(clazz: Class[_]): Logger = Logger(clazz)
}
