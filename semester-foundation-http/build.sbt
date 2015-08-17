name := "semester-foundation-http"

libraryDependencies ++= Seq(
  //"org.json4s" %% "json4s-native" % "latest.release",
  "org.json4s" %% "json4s-native" % "3.2.11",
//  "org.apache.httpcomponents" % "httpclient" % "latest.release",
//  "org.apache.httpcomponents" % "httpmime" % "latest.release"
  "org.apache.httpcomponents" % "httpclient" % "4.3.6",
  "org.apache.httpcomponents" % "httpmime" % "4.3.6"
)
