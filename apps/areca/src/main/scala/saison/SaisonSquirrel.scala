package saison

import squirrel.{SquirrelRecord, SquirrelCategory}

import java.nio.file.Path
import areca.Rule

object SaisonSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = SaisonRecord.fromFile(inputPath).map(fromSaison)
    )
  }

  def fromSaison(saison: SaisonRecord): SquirrelRecord = {
    SquirrelRecord(
      date = saison.date,
      description = saison.payMonth + ": " + saison.description,
      category = SquirrelCategory.fromTextRule(saison.description, "areca.saison.category"),
      value = -saison.amount,
      note = saison.note
    )
  }
}
