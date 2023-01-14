package android.com.demo.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ItemToSell")
class SellEntity (
    @PrimaryKey(autoGenerate = true)
    val uid: Int? = null,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "price") val price: Int?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
    @ColumnInfo(name = "type") val type: Int?
)