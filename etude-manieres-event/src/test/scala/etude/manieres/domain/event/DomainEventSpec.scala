package etude.manieres.domain.event

import akka.actor.ActorSystem
import etude.manieres.domain.model.{Entity, Identity}
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable.Specification

@RunWith(classOf[JUnitRunner])
class DomainEventSpec extends Specification {
  class NutId(val name: String) extends Identity[String] {
    def value: String = name
  }
  class Nut(val identity: NutId) extends Entity[NutId]

  "Event" should {
    "Be delivered" in {
      implicit val system = ActorSystem()

      Router() ! new EntityEvent(new NutId("almond"), new Nut(new NutId("almond")), EntityEventType.Store)

      1 should equalTo(1)
    }
  }
}
