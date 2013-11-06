package viewsnet

import areca.Rule
import java.nio.file.Path
import squirrel.{SquirrelRecord, SquirrelCategory}

object ViewsnetSquirrel extends Rule {
  def convert(inputPath: Path, outputPath: String): Unit = {
    SquirrelRecord.export(
      output = outputPath,
      records = ViewsnetRecord.fromFile(inputPath).map(fromViewsnet)
    )
  }

  def fromViewsnet(viewsnet: ViewsnetRecord): SquirrelRecord = {
    SquirrelRecord(
      date = viewsnet.date,
      description = viewsnet.description,
      category = SquirrelCategory.fromTextRule(viewsnet.description, "areca.viewsnet.category"),
      value = -viewsnet.amount,
      note = ""
    )
  }

}
