name := "etude-app-gare"

fork in run := true

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.9",
  "com.twitter" % "finagle-core_2.10" % "6.11.1",
  "com.twitter" % "finagle-http_2.10" % "6.11.1"
)
