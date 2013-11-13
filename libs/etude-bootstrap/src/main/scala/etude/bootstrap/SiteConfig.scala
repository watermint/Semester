package etude.bootstrap

case class SiteConfig(bootstrapBasePath: String,
                      jqueryPath: String,
                      bodyClass: Option[String] = None,
                      scripts: List[String] = List())