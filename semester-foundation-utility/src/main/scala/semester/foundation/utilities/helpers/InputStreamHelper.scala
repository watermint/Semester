package semester.foundation.utilities.helpers

import java.io.{ByteArrayInputStream, InputStream}

import scala.io.Source

object InputStreamHelper {
  case class InputStreamContainer(inputStream: InputStream) {
    def onMemory: ByteArrayInputStream = {
      new ByteArrayInputStream(asByteArray)
    }

    def asByteArray: Array[Byte] = {
      Source.fromInputStream(inputStream).map(_.toByte).toArray
    }
  }

  implicit def inputStreamToContainer(inputStream: InputStream): InputStreamContainer =
    InputStreamContainer(inputStream)
}
