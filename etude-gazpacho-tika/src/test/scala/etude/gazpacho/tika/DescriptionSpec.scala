package etude.gazpacho.tika

import java.nio.file.Paths

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DescriptionSpec
  extends Specification {

  "Description" should {
    "detect plain text" in {
      val d = Description.ofPath(Paths.get("build.sbt"))

      d.contentType must equalTo("text/plain; charset=ISO-8859-1")
      d.contentEncoding must equalTo(Some("ISO-8859-1"))
      d.isText must beTrue
      d.isImage must beFalse
    }

    for {
      (ext, mime) <- Map(
        "bmp" -> "image/x-ms-bmp",
        "gif" -> "image/gif",
        "jpg" -> "image/jpeg",
        "png" -> "image/png",
        "psd" -> "image/vnd.adobe.photoshop",
        "tif" -> "image/tiff"
      )
    } {
      s"detect $ext image as $mime" in {
        val d = Description.ofInputStream(getClass.getResourceAsStream(s"/test.$ext"))

        d.contentType must equalTo(mime)
        d.contentEncoding must beNone
        d.isText must beFalse
        d.isImage must beTrue
      }
    }
  }
}
