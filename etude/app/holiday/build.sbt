name := "etude-app-holiday"

fork in run := true

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.6",
  "com.twitter" %% "finatra" % "1.5.1"
)
