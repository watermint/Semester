name := "etude-elasticsearch-core"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.6",
  "org.elasticsearch" % "elasticsearch" % "1.0.0.Beta2",
  "org.elasticsearch" % "elasticsearch-analysis-kuromoji" % "1.6.0"
)
