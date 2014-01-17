name := "etude-elasticsearch-core"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.6",
  "org.elasticsearch" % "elasticsearch" % "1.0.0.RC1",
  "org.elasticsearch" % "elasticsearch-analysis-kuromoji" % "2.0.0.RC1"
)
