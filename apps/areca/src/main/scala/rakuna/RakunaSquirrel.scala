package rakuna

import areca.Rule
import java.nio.file.Path
import squirrel.{SquirrelCategory, SquirrelRecord}

/**
 *
 */
object RakunaSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = RakunaRecord.fromFile(inputPath).map(fromRakuna)
    )
  }

  def fromRakuna(rakuna: RakunaRecord): SquirrelRecord = {
    SquirrelRecord(
      date = rakuna.date,
      description = rakuna.description,
      category = SquirrelCategory.normalize(rakuna.category),
      value = rakuna.transactionType match {
        case s if s.contains("支出") => -rakuna.amount
        case _ => rakuna.amount
      },
      note = rakuna.subCategory
    )
  }
}
