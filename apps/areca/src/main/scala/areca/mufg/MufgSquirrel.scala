package areca.mufg

import areca.Rule
import java.nio.file.Path
import areca.squirrel.{SquirrelRecord, SquirrelCategory}

object MufgSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = MufgRecord.fromFile(inputPath).map(fromMufg)
    )
  }

  def fromMufg(mufg: MufgRecord): SquirrelRecord = {
    SquirrelRecord(
      date = mufg.date,
      description = mufg.description1 + ":" + mufg.description2,
      category = SquirrelCategory.fromTextRule(mufg.description1, "areca.mufg.category"),
      value = mufg.deposit.getOrElse(0) - mufg.withdraw.getOrElse(0),
      note =
        "残高: " + mufg.balance + "; " +
          "メモ: " + mufg.note + "; " +
          "未資金化区分:" + mufg.fundCategory + "; " +
          "入払区分:" + mufg.transactionCategory
    )
  }

}
