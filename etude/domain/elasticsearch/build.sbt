name := "etude-domain-elasticsearch"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.9",
  "org.elasticsearch" % "elasticsearch" % "1.1.1",
  "org.elasticsearch" % "elasticsearch-analysis-kuromoji" % "2.1.0"
)
