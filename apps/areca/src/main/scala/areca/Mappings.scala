package areca

import areca.emoney.EmoneyMapping
import areca.mufg.MufgMapping
import areca.rakuna.RakunaMapping
import areca.saison.SaisonMapping
import areca.shinsei.ShinseiMapping
import areca.smbc.SmbcMapping
import areca.starbucks.StarbucksMapping
import areca.suruga.SurugaMapping
import areca.viewsnet.ViewsnetMapping

object Mappings {
  val mappings: Map[String, Mapping] = Map(
    "emoney" -> EmoneyMapping,
    "areca/mufg" -> MufgMapping,
    "areca/rakuna" -> RakunaMapping,
    "areca/saison" -> SaisonMapping,
    "areca/shinsei" -> ShinseiMapping,
    "areca/smbc" -> SmbcMapping,
    "areca/starbucks" -> StarbucksMapping,
    "areca/suruga" -> SurugaMapping,
    "areca/viewsnet" -> ViewsnetMapping
  )
}

