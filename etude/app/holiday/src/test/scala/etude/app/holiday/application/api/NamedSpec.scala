package etude.app.holiday.application.api

import com.twitter.finatra.test.FlatSpecHelper
import com.twitter.finatra.FinatraServer
import etude.app.holiday.application.App

class NamedSpec extends FlatSpecHelper {
  override def server: FinatraServer = App

  "GET /named/sample/2014-01-01" should "respond 200 with true" in {
    get("/named/sample/2014-01-01")
    response.code should equal(200)
    response.body should equal("""{"holiday":true}""")
  }

  "GET /named/sample/2014-01-02" should "respond 200 with true" in {
    get("/named/sample/2014-01-02")
    response.code should equal(200)
    response.body should equal("""{"holiday":true}""")
  }

  "GET /named/sample/2014-01-06" should "respond 200 with false" in {
    get("/named/sample/2014-01-06")
    response.code should equal(200)
    response.body should equal("""{"holiday":false}""")
  }

}
