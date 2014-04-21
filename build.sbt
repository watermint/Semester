
lazy val buildSettings = Seq(
  version := "0.1.9",
  organization := "org.watermint",
  scalaVersion := "2.10.4",
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "twitter-repo" at "http://maven.twttr.com",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1",
    "org.slf4j" % "slf4j-simple" % "1.7.6",
    "org.specs2" %% "specs2" % "2.3.7" % "test",
    "junit" % "junit" % "4.11" % "test"
  )
)

//lazy val appCulvert = project.in(file("etude/app/culvert"))
//  .dependsOn(foundationDomain)
//  .dependsOn(messagingChatwork)
//  .settings(buildSettings: _*)
//  .settings(assemblySettings: _*)

lazy val appFedelini = project.in(file("etude/app/fedelini"))
  .dependsOn(messagingChatwork)
  .dependsOn(ticketThings)
  .settings(buildSettings: _*)

lazy val appGare = project.in(file("etude/app/gare"))
  .dependsOn(foundationDomain)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

//lazy val appHoliday = project.in(file("etude/app/holiday"))
//  .settings(buildSettings: _*)
//  .settings(assemblySettings: _*)
//  .dependsOn(foundationCalendar)

lazy val bookmarkPocket = project.in(file("etude/bookmark/pocket"))
  .dependsOn(foundationDomain)
  .dependsOn(foundationHttp)
  .settings(buildSettings: _*)

lazy val messagingChatwork = project.in(file("etude/messaging/chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(foundationDomain)
  .dependsOn(foundationHttp)
  .dependsOn(foundationHtml)
  .dependsOn(foundationUtility)
  .dependsOn(testUndisclosed % "test")

//lazy val elasticsearchCore = project.in(file("etude/elasticsearch/core"))
//  .settings(buildSettings: _*)
//  .dependsOn(foundationUtility)

lazy val foundationDomain = project.in(file("etude/foundation/domain"))
  .settings(buildSettings: _*)

//lazy val foundationCalendar = project.in(file("etude/foundation/calendar"))
//  .settings(buildSettings: _*)
//  .dependsOn(foundationDomain)
//  .dependsOn(foundationHttp)
//  .dependsOn(foundationI18n)

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

lazy val ticketThings = project.in(file("etude/ticket/things"))
  .settings(buildSettings: _*)

lazy val testUndisclosed = project.in(file("etude/test/undisclosed"))
  .settings(buildSettings: _*)

