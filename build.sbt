
lazy val buildSettings = Seq(
  version := "0.7.10",
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

// ---- table

lazy val tableArrabbiata = project.in(file("etude-table-arrabbiata"))
  .dependsOn(pintxosChatwork)
  .dependsOn(pintxosThings)
  .dependsOn(gazpachoLogging)
  .dependsOn(vinoChatwork)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)

lazy val tableBolognese = project.in(file("etude-table-bolognese"))
  .dependsOn(gazpachoLogging)
  .dependsOn(gazpachoSpray)
  .settings(buildSettings: _*)
  .settings(assemblySettings: _*)


// ---- pintxos
lazy val pintxosPocket = project.in(file("etude-pintxos-pocket"))
  .dependsOn(domainCore)
  .dependsOn(gazpachoUtility)
  .dependsOn(gazpachoLogging)
  .dependsOn(gazpachoHttp)
  .dependsOn(gazpachoSpray)
  .settings(buildSettings: _*)

lazy val pintxosThings = project.in(file("etude-pintxos-things"))
  .settings(buildSettings: _*)

lazy val pintxosChatwork = project.in(file("etude-pintxos-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(gazpachoHtml)
  .dependsOn(gazpachoHttp)
  .dependsOn(gazpachoUtility)
  .dependsOn(gazpachoLogging)
  .dependsOn(gazpachoUndisclosed % "test")


// ---- DDD
lazy val domainCore = project.in(file("etude-domain-core"))
  .settings(buildSettings: _*)

lazy val domainElasticSearch = project.in(file("etude-domain-elasticsearch"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(gazpachoElasticSearch)
  .dependsOn(gazpachoLogging)


// ---- gazpacho
lazy val gazpachoHtml = project.in(file("etude-gazpacho-html"))
  .settings(buildSettings: _*)

lazy val gazpachoLogging = project.in(file("etude-gazpacho-logging"))
  .settings(buildSettings: _*)

lazy val gazpachoHttp = project.in(file("etude-gazpacho-http"))
  .settings(buildSettings: _*)
  .dependsOn(gazpachoUtility)
  .dependsOn(gazpachoLogging)

lazy val gazpachoUtility = project.in(file("etude-gazpacho-utility"))
  .settings(buildSettings: _*)

lazy val gazpachoFextile = project.in(file("etude-gazpacho-fextile"))
  .settings(buildSettings: _*)
  .dependsOn(gazpachoLogging)

lazy val gazpachoHighlight = project.in(file("etude-gazpacho-highlight"))
  .settings(buildSettings: _*)

lazy val gazpachoSpray = project.in(file("etude-gazpacho-spray"))
  .settings(buildSettings: _*)

lazy val gazpachoElasticSearch = project.in(file("etude-gazpacho-elasticsearch"))
  .settings(buildSettings: _*)

lazy val gazpachoTika = project.in(file("etude-gazpacho-tika"))
  .settings(buildSettings: _*)

lazy val gazpachoUndisclosed = project.in(file("etude-gazpacho-undisclosed"))
  .settings(buildSettings: _*)
  .dependsOn(gazpachoLogging)


// ---- kitchenette
lazy val vinoChatwork = project.in(file("etude-vino-chatwork"))
  .settings(buildSettings: _*)
  .dependsOn(pintxosChatwork)
  .settings(assemblySettings: _*)

lazy val vinoCode = project.in(file("etude-vino-code"))
  .settings(buildSettings: _*)
  .dependsOn(domainCore)
  .dependsOn(domainElasticSearch)
  .dependsOn(gazpachoTika)
  .dependsOn(gazpachoHighlight)
  .dependsOn(gazpachoElasticSearch)

