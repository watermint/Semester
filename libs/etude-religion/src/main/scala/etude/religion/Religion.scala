package etude.religion


case class Religion(id: String)

object Religion {
  /**
   * Note: This list is not intended to cover all religions.
   * This list is based on religions which names are listed on the page,
   * [The Global Religious Landscape](http://www.pewforum.org/2012/12/18/global-religious-landscape-exec/).
   */
  val buddhism = Religion("buddhism")
  val christian = Religion("christian")
  val hinduism = Religion("hinduism")
  val islamic = Religion("islamic")
  val jewish = Religion("jewish")
}
