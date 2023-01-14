package android.com.demo.data.room.dao

import android.com.demo.data.room.entity.SellEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SellDao {
    @Query("SELECT * FROM ItemToSell")
    suspend fun getAll(): List<SellEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SellEntity) : Long
}