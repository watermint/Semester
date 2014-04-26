
lazy val buildSettings = Seq(
  version := "0.3.0",
  organization := "org.watermint",
  scalaVersion := "2.11.0",
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "twitter-repo" at "http://maven.twttr.com",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-simple" % "latest.integration",
    "org.slf4j" % "jcl-over-slf4j" % "latest.integration",
    "org.slf4j" % "log4j-over-slf4j" % "latest.integration",
    "ch.qos.logback" % "logback-classic" % "latest.integration",
    "org.specs2" %% "specs2" % "latest.integration" % "test",
    "junit" % "junit" % "latest.integration" % "test"
  )
)

lazy val appFedelini = project.in(file("etude/app/fedelini"))
  .dependsOn(messagingChatwork)
  .dependsOn(ticketThings)
  .settings(buildSettings: _*)

lazy val appGare = project.in(file("etude/app/gare"))
  .dependsOn(domainCore)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

lazy val bookmarkPocket = project.in(file("etude/bookmark/pocket"))
  .dependsOn(domainCore)
  .dependsOn(foundationUtility)
  .dependsOn(foundationHttp)
  .settings(buildSettings: _*)

lazy val messagingChatwork = project.in(file("etude/messaging/chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(foundationHttp)
  .dependsOn(foundationHtml)
  .dependsOn(foundationUtility)
  .dependsOn(testUndisclosed % "test")

lazy val domainCore = project.in(file("etude/domain/core"))
  .settings(buildSettings: _*)

lazy val domainElasticsearch = project.in(file("etude/domain/elasticsearch"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)

lazy val foundationHtml = project.in(file("etude/foundation/html"))
  .settings(buildSettings: _*)

lazy val foundationHttp = project.in(file("etude/foundation/http"))
  .settings(buildSettings: _*)
  .dependsOn(foundationUtility)

lazy val foundationI18n = project.in(file("etude/foundation/i18n"))
  .settings(buildSettings: _*)

lazy val foundationUtility = project.in(file("etude/foundation/utility"))
  .settings(buildSettings: _*)

lazy val desktopFextile = project.in(file("etude/desktop/fextile"))
  .settings(buildSettings: _*)

lazy val kitchenetteChatwork = project.in(file("etude/kitchenette/chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(messagingChatwork)

lazy val ticketThings = project.in(file("etude/ticket/things"))
  .settings(buildSettings: _*)

lazy val testUndisclosed = project.in(file("etude/test/undisclosed"))
  .settings(buildSettings: _*)

