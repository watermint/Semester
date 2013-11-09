package areca.shinsei

import areca.Rule
import java.nio.file.Path
import areca.squirrel.{SquirrelRecord, SquirrelCategory}

/**
  */
object ShinseiSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = ShinseiRecord.fromFile(inputPath).map(fromShinsei)
    )
  }

  def fromShinsei(shinsei: ShinseiRecord): SquirrelRecord = {
    SquirrelRecord(
      date = shinsei.date,
      description = shinsei.description,
      category = SquirrelCategory.fromTextRule(shinsei.description, "areca.shinsei.category"),
      value = shinsei.deposit.getOrElse(0) - shinsei.withdrawal.getOrElse(0),
      note = "残高: " + shinsei.balance
    )
  }
}
