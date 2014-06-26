name := "etude-app-arrabbiata"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "latest.integration",
  "org.controlsfx" % "controlsfx" % "latest.integration",
  "com.typesafe.akka" %% "akka-actor" % "latest.integration"
)

packageOptions in (Compile, packageBin) +=
  Package.ManifestAttributes(
    "Specification-Title" -> "Java 8u20",
    "Specification-Version" -> "8.0.20"
  )

