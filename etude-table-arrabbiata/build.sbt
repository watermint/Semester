name := "etude-table-arrabbiata"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "latest.release",
  "org.controlsfx" % "controlsfx" % "latest.release",
  "com.typesafe.akka" %% "akka-actor" % "latest.release"
)

packageOptions in (Compile, packageBin) +=
  Package.ManifestAttributes(
    "Specification-Title" -> "Java 8u20",
    "Specification-Version" -> "8.0.20"
  )

