package etude.epice.sel.request

import java.io.{File, InputStream}
import java.nio.charset.Charset

class Multipart extends Payload {
  def withCharset(charset: Charset): Multipart = ???

  def withBinary(name: String, binary: Array[Byte]): Multipart = ???

  def withBinary(name: String, binary: InputStream): Multipart = ???

  def withBinary(name: String, binary: File): Multipart = ???

  def withText(name: String, text: String): Multipart = ???
}
