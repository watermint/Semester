
lazy val buildSettings = Seq(
  version := "0.0.14",
  organization := "org.watermint",
  scalaVersion := "2.10.2",
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "twitter-repo" at "http://maven.twttr.com",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.13",
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.specs2" %% "specs2" % "2.2.3" % "test",
    "junit" % "junit" % "4.7" % "test"
  )
)


lazy val appsAreca = project.in(file("apps/areca"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)
  .dependsOn(etudeCommons)

lazy val appsStenographer = project.in(file("apps/stenographer"))
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)
  .dependsOn(etudeBootstrap)
  .dependsOn(etudeChatwork)
  .dependsOn(etudeFextile)

lazy val etudeBootstrap = project.in(file("libs/etude-bootstrap"))
  .settings(buildSettings: _*)

lazy val etudeChatwork = project.in(file("libs/etude-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(etudeCommons)

lazy val etudeCommons = project.in(file("libs/etude-commons"))
  .settings(buildSettings: _*)

lazy val etudeFextile = project.in(file("libs/etude-fextile"))
  .settings(buildSettings: _*)

