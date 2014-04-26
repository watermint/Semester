name := "etude-app-gare"

fork in run := true

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "latest.integration",
  "com.twitter" % "finagle-core_2.10" % "latest.integration",
  "com.twitter" % "finagle-http_2.10" % "latest.integration"
)
