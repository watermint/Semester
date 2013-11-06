package smbc

import areca.Rule
import java.nio.file.Path
import squirrel.{SquirrelCategory, SquirrelRecord}

object SmbcSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = SmbcRecord.fromFile(inputPath).map(fromSmbc)
    )
  }

  def fromSmbc(smbc: SmbcRecord): SquirrelRecord = {
    SquirrelRecord(
      date = smbc.date,
      description = smbc.description,
      category = SquirrelCategory.fromTextRule(smbc.description, "areca.smbc.category"),
      value = smbc.deposit.getOrElse(0) - smbc.withdraw.getOrElse(0),
      note = "残高: " + smbc.balance
    )
  }
}
