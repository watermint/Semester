package etude.io

import java.io.{ByteArrayInputStream, InputStream}
import scalax.io.Resource

/**
 *
 */
object Memory {
  case class InputStreamContainer(inputStream: InputStream) {
    def onMemory: ByteArrayInputStream = {
      new ByteArrayInputStream(Resource.fromInputStream(inputStream).byteArray)
    }
  }

  implicit def inputStreamToContainer(inputStream: InputStream): InputStreamContainer =
    InputStreamContainer(inputStream)
}
