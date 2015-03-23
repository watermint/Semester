package semester.service.box

import java.nio.file.{Files, Path}

import com.box.sdk.BoxAPIConnection
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

case class AuthToken(accessToken: String,
                     refreshToken: String) {

  def write(path: Path): Unit = {
    Files.write(path, Seq(json))
  }

  lazy val json: String = {
    implicit val formats = DefaultFormats
    Serialization.write(this)
  }
}

object AuthToken {
  def read(path: Path): AuthToken = {
    implicit val formats = DefaultFormats
    val json = Files.readAllLines(path).asScala.mkString

    Serialization.read[AuthToken](json)
  }

  def apply(box: BoxAPIConnection): AuthToken = {
    AuthToken(
      box.getAccessToken,
      box.getRefreshToken
    )
  }
}