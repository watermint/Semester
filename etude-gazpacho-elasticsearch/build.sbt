name := "etude-gazpacho-elasticsearch"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "latest.integration",
  "org.elasticsearch" % "elasticsearch" % "latest.integration",
  "org.elasticsearch" % "elasticsearch-analysis-kuromoji" % "latest.integration"
)

