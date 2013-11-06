package areca

import java.nio.file.{Files, FileSystems, Path}

case class Operation(sourceType: Option[String] = None,
                     sourcePath: Option[Path] = None,
                     destType: Option[String] = None,
                     destPath: Option[String] = None)

object Operation {
  lazy val sourceTypes: List[String] = Mappings.mappings.keys.toList

  lazy val destTypes: List[String] = Mappings.mappings.values.flatMap(_.keys).toList.distinct

  lazy val textSourceTypes = sourceTypes.mkString(", ")

  lazy val textDestTypes = destTypes.mkString(", ")

  val argParser = new scopt.OptionParser[Operation]("areca") {
    head("areca", "1.0")

    //
    // Source Type
    //
    opt[String]('s', "source") action {
      case (typeName, o) =>
        sourceTypes.contains(typeName) match {
          case true => o.copy(sourceType = Some(typeName))
          case _ => o
        }
    } validate {
      typeName =>
        sourceTypes.contains(typeName) match {
          case true => success
          case _ => failure("unknown type. supported types are follows: " + textSourceTypes)
        }
    } text {
      "source file type: " + textSourceTypes
    } required()

    //
    // Source File Path
    //
    opt[String]('i', "input") action {
      case (input, o) =>
        o.copy(
          sourcePath = Some(FileSystems.getDefault.getPath(input))
        )
    } validate {
      input =>
        val path = FileSystems.getDefault.getPath(input)
        if (Files.exists(path)) {
          success
        } else {
          failure("file not found: " + path.toString)
        }
    } text {
      "input file name"
    } required()

    //
    // Dest Type
    //
    opt[String]('d', "dest") action {
      case (dest, o) =>
        destTypes.contains(dest) match {
          case true => o.copy(destType = Some(dest))
          case _ => o
        }
    } validate {
      dest =>
        destTypes.contains(dest) match {
          case true => success
          case _ => failure("unknown dest type: " + dest + ", supported types are: " + textDestTypes)
        }
    } text {
      "destination type: " + textDestTypes
    } required()

    //
    // Output file
    //
    opt[String]('o', "output") action {
      case (output, o) =>
        o.copy(destPath = Some(output))
    } text {
      "output file name"
    } required()
  }
}