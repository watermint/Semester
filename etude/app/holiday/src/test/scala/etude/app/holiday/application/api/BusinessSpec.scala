package etude.app.holiday.application.api

import com.twitter.finatra.test.FlatSpecHelper
import com.twitter.finatra.FinatraServer
import etude.app.holiday.application.App
import java.io.File

class BusinessSpec extends FlatSpecHelper {
  override def server: FinatraServer = new App(new File("etude/app/holiday/src/test/data/calendars.json"))

  "GET /business/sample-japan/2014-01-01" should "respond 200 with true" in {
    get("/business/sample-japan/2014-01-01")
    response.code should equal(200)
    response.body should equal("""{"holiday":true}""")
  }

  "GET /business/sample-japan/2014-01-02" should "respond 200 with true" in {
    get("/business/sample-japan/2014-01-02")
    response.code should equal(200)
    response.body should equal("""{"holiday":true}""")
  }

  "GET /business/sample-japan/2014-01-06" should "respond 200 with false" in {
    get("/business/sample-japan/2014-01-06")
    response.code should equal(200)
    response.body should equal("""{"holiday":false}""")
  }

}
