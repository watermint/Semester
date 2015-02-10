package semester.service.domain.infrastructure.auth

import java.nio.charset.StandardCharsets
import java.nio.file.attribute.{PosixFilePermission, PosixFilePermissions}
import java.nio.file.{Files, Paths}

import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.native.JsonMethods
import org.json4s.native.JsonMethods._

import scala.collection.JavaConverters._
import scala.util.{Success, Try}

case class AuthSession(userName: String,
                       accessToken: String,
                       consumerKey: String)

object AuthSession {

  val sessionDirectory = Paths.get(System.getProperty("user.home"), ".semester-pocket")
  val sessionFile = sessionDirectory.resolve("session.json")

  def storeSession(session: AuthSession): Unit = {
    if (!Files.isDirectory(sessionDirectory)) {
      Files.createDirectories(
        sessionDirectory,
        PosixFilePermissions.asFileAttribute(
          java.util.EnumSet.of(
            PosixFilePermission.OWNER_READ,
            PosixFilePermission.OWNER_WRITE,
            PosixFilePermission.OWNER_EXECUTE
          )
        )
      )
    }

    val content = Map(
      "userName" -> session.userName,
      "consumerKey" -> session.consumerKey,
      "accessToken" -> session.accessToken
    )

    Files.write(
      sessionFile,
      compact(render(content)).getBytes(StandardCharsets.UTF_8)
    )
  }

  def loadSession(): Try[AuthSession] = {
    val json = JsonMethods.parse(Files.readAllLines(sessionFile).asScala.mkString)
    val result: Seq[AuthSession] = for {
      JObject(content) <- json
      JField("userName", JString(userName)) <- content
      JField("consumerKey", JString(consumerKey)) <- content
      JField("accessToken", JString(accessToken)) <- content
    } yield {
      AuthSession(
        userName = userName,
        accessToken = accessToken,
        consumerKey = consumerKey
      )
    }

    Success(result.last)
  }
}