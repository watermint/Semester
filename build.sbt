
lazy val buildSettings = Seq(
  version := "0.7.9",
  organization := "org.watermint",
  scalaVersion := "2.11.1",
  crossScalaVersions := Seq("2.11.1"),
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

// ---- apps

lazy val appArrabbiata = project.in(file("etude-app-arrabbiata"))
  .dependsOn(adapterChatwork)
  .dependsOn(adapterThings)
  .dependsOn(kitchenetteFedelini)
  .dependsOn(foundationLogging)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)


// ---- adapter
lazy val adapterPocket = project.in(file("etude-adapter-pocket"))
  .dependsOn(domainCore)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)
  .dependsOn(foundationHttp)
  .dependsOn(paupietteSpray)
  .settings(buildSettings: _*)

lazy val adapterThings = project.in(file("etude-adapter-things"))
  .settings(buildSettings: _*)

lazy val adapterChatwork = project.in(file("etude-adapter-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(paupietteHtml)
  .dependsOn(foundationHttp)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)
  .dependsOn(testUndisclosed % "test")


// ---- DDD
lazy val domainCore = project.in(file("etude-domain-core"))
  .settings(buildSettings: _*)

lazy val domainElasticSearch = project.in(file("etude-domain-elasticsearch"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(paupietteElasticSearch)
  .dependsOn(foundationLogging)


// ---- foundation
lazy val foundationLogging = project.in(file("etude-foundation-logging"))
  .settings(buildSettings: _*)

lazy val foundationHttp = project.in(file("etude-foundation-http"))
  .settings(buildSettings: _*)
  .dependsOn(foundationUtility)
  .dependsOn(foundationLogging)

lazy val foundationUtility = project.in(file("etude-foundation-utility"))
  .settings(buildSettings: _*)


// ---- desktop
lazy val desktopFextile = project.in(file("etude-desktop-fextile"))
  .settings(buildSettings: _*)
  .dependsOn(foundationLogging)


// ---- kitchenette
lazy val kitchenetteFedelini = project.in(file("etude-kitchenette-fedelini"))
  .settings(buildSettings: _*)
  .dependsOn(adapterChatwork)
  .settings(assemblySettings: _*)

lazy val kitchenetteCode = project.in(file("etude-kitchenette-code"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(domainElasticSearch)
  .dependsOn(paupietteTika)
  .dependsOn(paupietteHighlight)
  .dependsOn(paupietteElasticSearch)

// ---- paupiette

lazy val paupietteHtml = project.in(file("etude-paupiette-html"))
  .settings(buildSettings: _*)

lazy val paupietteHighlight = project.in(file("etude-paupiette-highlight"))
  .settings(buildSettings: _*)

lazy val paupietteSpray = project.in(file("etude-paupiette-spray"))
  .settings(buildSettings: _*)

lazy val paupietteElasticSearch = project.in(file("etude-paupiette-elasticsearch"))
  .settings(buildSettings: _*)

lazy val paupietteTika = project.in(file("etude-paupiette-tika"))
  .settings(buildSettings: _*)

// ---- test
lazy val testUndisclosed = project.in(file("etude-test-undisclosed"))
  .settings(buildSettings: _*)
  .dependsOn(foundationLogging)


// ---- recherche
lazy val rechercheBolognese = project.in(file("etude-recherche-bolognese"))
  .dependsOn(foundationLogging)
  .dependsOn(paupietteSpray)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

