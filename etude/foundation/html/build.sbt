name := "etude-foundation-html"

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "latest.integration",
  "nu.validator.htmlparser" % "htmlparser" % "latest.integration"
)

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value :+ "org.scala-lang.modules" %% "scala-xml" % "latest.integration"
    case _ =>
      libraryDependencies.value
  }
}
