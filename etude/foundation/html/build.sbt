name := "etude-foundation-html"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "latest.integration", // for 2.11
//  "commons-codec" % "commons-codec" % "latest.integration",
  "org.jsoup" % "jsoup" % "latest.integration",
  "nu.validator.htmlparser" % "htmlparser" % "latest.integration"
)
