package etude.data.chintai.domain

import java.time.Year
import etude.foundation.domain.model.Entity

/**
 * 物件エンティティ.
 *
 * @param identity 物件ID.
 * @param address 住所.
 * @param floor 階.
 * @param title タイトル.
 * @param yearOfBuilt 築年.
 * @param footprint 専有面積(sq. meter).
 * @param rentFee 賃料(円).
 * @param adminCost 管理費／共益費(円).
 * @param keyMoney 礼金(円).
 * @param deposit 敷金(円).
 * @param approach 交通.
 */
class Property(val identity: PropertyId,
               val address: Address,
               val floor: Int,
               val title: String,
               val yearOfBuilt: Year,
               val footprint: Double,
               val rentFee: Int,
               val adminCost: Int,
               val keyMoney: Int,
               val deposit: Int,
               val approach: String)
  extends Entity[PropertyId] {
}
