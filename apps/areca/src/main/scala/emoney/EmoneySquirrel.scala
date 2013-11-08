package emoney

import squirrel.{SquirrelRecord, SquirrelCategory}
import java.nio.file.Path
import areca.{RuleConfig, Rule}

object EmoneySquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String) = {
    SquirrelRecord.export(
      output = outputPath,
      records = EmoneyRecord.fromFile(inputPath).map(fromEmoney)
    )
  }

  def fromEmoney(emoney: EmoneyRecord): SquirrelRecord = {
    SquirrelRecord(
      date = emoney.date,
      description = emoney.description,
      category = SquirrelCategory.fromTextRule(emoney.description, "areca.emoney.category"),
      value = value(emoney),
      note = emoney.note
    )
  }

  def value(emoney: EmoneyRecord): Int = {
    RuleConfig.getList("areca.emoney.deposit").exists(emoney.description.contains(_)) match {
      case true => emoney.value
      case _ => -emoney.value
    }
  }

}