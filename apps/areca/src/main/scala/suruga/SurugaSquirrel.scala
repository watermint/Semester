package suruga

import areca.Rule
import java.nio.file.Path
import squirrel.{SquirrelRecord, SquirrelCategory}

object SurugaSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = SurugaRecord.fromFile(inputPath).map(fromSuruga)
    )
  }

  def fromSuruga(suruga: SurugaRecord): SquirrelRecord = {
    SquirrelRecord(
      date = suruga.date,
      description = suruga.description,
      category = SquirrelCategory.fromTextRule(suruga.description, "areca.suruga.category"),
      value = suruga.deposit.getOrElse(0) - suruga.withdraw.getOrElse(0),
      note = "\"残高: " + suruga.balance +
        ", 取引区分: " + suruga.transactionCategory +
        ", メモ: \"" + suruga.note
    )
  }
}
