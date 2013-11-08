package emoney

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import com.typesafe.config.ConfigFactory
import java.nio.file.FileSystems
import emoney.{EmoneySquirrel, EmoneyRecord}
import squirrel.SquirrelCategory

@RunWith(classOf[JUnitRunner])
class EmoneySpec extends Specification {
  "Emoney" should {
    "Have default mapping" in {
      val config = ConfigFactory.load()
      config.getObject("areca.emoney").toOption must beSome
    }

    "Load record from csv" in {
      val path = FileSystems.getDefault.getPath("apps/areca/src/test/resources/emoney/source01.csv")
      val records = EmoneyRecord.fromFile(path)

      records must haveSize(9)

      EmoneySquirrel.fromEmoney(records(0)).category must equalTo(SquirrelCategory.category("food"))
      EmoneySquirrel.fromEmoney(records(2)).category must equalTo(SquirrelCategory.category("transport"))
      EmoneySquirrel.fromEmoney(records(0)).value must equalTo(-158)
      EmoneySquirrel.fromEmoney(records(3)).value must equalTo(3000)
    }
  }
}
