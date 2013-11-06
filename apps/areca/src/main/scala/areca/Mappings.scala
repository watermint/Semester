package areca

import emoneyreader.EmoneyMapping
import saison.SaisonMapping
import starbucks.StarbucksMapping
import shinsei.ShinseiMapping
import smbc.SmbcMapping
import suruga.SurugaMapping
import viewsnet.ViewsnetMapping
import mufg.MufgMapping
import rakuna.RakunaMapping

object Mappings {
  val mappings: Map[String, Mapping] = Map(
    "emoney" -> EmoneyMapping,
    "mufg" -> MufgMapping,
    "rakuna" -> RakunaMapping,
    "saison" -> SaisonMapping,
    "shinsei" -> ShinseiMapping,
    "smbc" -> SmbcMapping,
    "suruga" -> SurugaMapping,
    "starbucks" -> StarbucksMapping,
    "viewsnet" -> ViewsnetMapping
  )
}

