package etude.app.holiday.application.api

import com.twitter.finatra.test.FlatSpecHelper
import etude.app.holiday.application.App
import com.twitter.finatra.FinatraServer
import java.io.File

class CountrySpec extends FlatSpecHelper {
  override def server: FinatraServer = new App(new File("etude/app/holiday/src/test/data/calendars.json"))

  "GET /country/JP/2014-01-01" should "respond 200 with true" in {
    get("/country/JP/2014-01-01")
    response.code should equal(200)
    response.body should equal("""{"holiday":true}""")
  }

  "GET /country/JP/2014-01-02" should "respond 200 with false" in {
    get("/country/JP/2014-01-02")
    response.code should equal(200)
    response.body should equal("""{"holiday":false}""")
  }

  "GET /country/JP/1999-01-01" should "respond 404" in {
    get("/country/JP/1999-01-01")
    response.code should equal(404)
  }

  "GET /country/ZZ/2014-01-01" should "respond 404" in {
    get("/country/ZZ/2014-01-01")
    response.code should equal(404)
  }
}
