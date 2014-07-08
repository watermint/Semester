
lazy val buildSettings = Seq(
  version := "0.7.2",
  organization := "org.watermint",
  scalaVersion := "2.11.1",
  crossScalaVersions := Seq("2.11.1", "2.10.4"),
  resolvers ++= Seq(
    "sonatype-public" at "https://oss.sonatype.org/content/groups/public",
    "atlassian-public" at "https://maven.atlassian.com/repository/public",
    "maven-central" at "http://repo1.maven.org/maven2",
    "twitter-repo" at "http://maven.twttr.com",
    Resolver.sonatypeRepo("snapshots")
  ),
  libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "latest.integration" % "test",
    "junit" % "junit" % "latest.integration" % "test"
  ),
  ivyXML :=
    <dependencies>
      <exclude org="log4j" name="log4j" />
      <exclude org="commons-logging" name="commons-logging" />
      <exclude org="org.slf4j" name="slf4j-log4j12" />
    </dependencies>
)


lazy val appArrabbiata = project.in(file("etude/app/arrabbiata"))
  .dependsOn(messagingChatwork)
  .dependsOn(kitchenetteFedelini)
  .dependsOn(ticketThings)
  .dependsOn(foundationLogging)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

lazy val bookmarkPocket = project.in(file("etude/bookmark/pocket"))
  .dependsOn(domainCore)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)
  .dependsOn(foundationHttp)
  .dependsOn(kitchenetteSpray)
  .settings(buildSettings: _*)

lazy val messagingChatwork = project.in(file("etude/messaging/chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(foundationHttp)
  .dependsOn(foundationHtml)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)
  .dependsOn(testUndisclosed % "test")

lazy val domainCore = project.in(file("etude/domain/core"))
  .settings(buildSettings: _*)

lazy val domainJson = project.in(file("etude/domain/json"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)

lazy val domainElasticsearch = project.in(file("etude/domain/elasticsearch"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(domainJson)

lazy val foundationLogging = project.in(file("etude/foundation/logging"))
  .settings(buildSettings: _*)

lazy val foundationHtml = project.in(file("etude/foundation/html"))
  .settings(buildSettings: _*)

lazy val foundationHttp = project.in(file("etude/foundation/http"))
  .settings(buildSettings: _*)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)

lazy val foundationUtility = project.in(file("etude/foundation/utility"))
  .settings(buildSettings: _*)

lazy val desktopFextile = project.in(file("etude/desktop/fextile"))
  .settings(buildSettings: _*)
  .dependsOn(foundationLogging)

lazy val kitchenetteFedelini = project.in(file("etude/kitchenette/fedelini"))
  .settings(buildSettings: _*)
  .dependsOn(messagingChatwork)
  .settings(assemblySettings: _*)

lazy val kitchenetteSpray = project.in(file("etude/kitchenette/spray"))
  .settings(buildSettings: _*)
  .dependsOn(messagingChatwork)

lazy val ticketThings = project.in(file("etude/ticket/things"))
  .settings(buildSettings: _*)

lazy val testUndisclosed = project.in(file("etude/test/undisclosed"))
  .settings(buildSettings: _*)
  .dependsOn(foundationLogging)

lazy val rechercheBolognese = project.in(file("etude/recherche/bolognese"))
  .dependsOn(foundationLogging)
  .dependsOn(kitchenetteSpray)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

