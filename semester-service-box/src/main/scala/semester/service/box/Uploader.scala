package semester.service.box

import java.io.FileInputStream
import java.nio.file.{Paths, Files, Path}

import com.box.sdk.{BoxFolder, BoxAPIConnection}
import org.json4s.DefaultFormats
import org.json4s.native.Serialization
import scopt.OptionParser

object Uploader extends AppConfig {

  case class Operation(inputFile: Option[String] = None,
                       tokenFile: Option[String] = None,
                       destination: Option[String] = None)

  def connection(token: AuthToken): BoxAPIConnection = {
    new BoxAPIConnection(clientId, clientSecret, token.accessToken, token.refreshToken)
  }

  def connection(operation: Operation): BoxAPIConnection = {
    connection(AuthToken.read(Paths.get(operation.tokenFile.get)))
  }

  val argParser = new OptionParser[Operation]("box uploader") {
    head("box uploader")

    opt[String]('f', "file") action {
      case (i, o) =>
        o.copy(inputFile = Some(i))
    } validate {
      i =>
        Files.exists(Paths.get(i)) match {
          case true => success
          case _ => failure(s"File not found: $i")
        }
    } text {
      "Upload file path"
    } required()

    opt[String]('d', "dest") action {
      case (i, o) =>
        o.copy(destination = Some(i))
    } text {
      "Destination folder Id"
    } required()

    opt[String]('t', "token") action {
      case (i, o) =>
        o.copy(tokenFile = Some(i))
    } validate {
      i =>
        try {
          AuthToken.read(Paths.get(i))
          success
        } catch {
          case e: Exception =>
            failure(s"Error on loading file[$i]: $e")
        }
    } text {
      "Token file"
    } required()
  }

  def main(args: Array[String]): Unit = {
    argParser.parse(args, Operation()) map {
      operation =>
        val box = connection(operation)
        val folder = new BoxFolder(box, operation.destination.get)
        val uploadFile = Paths.get(operation.inputFile.get)
        val uploadFileStream = new FileInputStream(uploadFile.toFile)

        try {
          box.refresh()
          val upload = folder.uploadFile(uploadFileStream, uploadFile.getFileName.toString)
          println(upload.getID)
        } finally {
          AuthToken(box).write(Paths.get(operation.tokenFile.get))
          uploadFileStream.close()
        }
    }
  }
}
