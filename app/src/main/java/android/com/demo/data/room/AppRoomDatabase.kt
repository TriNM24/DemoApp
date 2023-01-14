package android.com.demo.data.room

import android.com.demo.data.room.dao.SellDao
import android.com.demo.data.room.entity.SellEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [SellEntity::class],
    version = 1
)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun sellDao(): SellDao

    class Callback @Inject constructor(
        private val database: Provider<AppRoomDatabase>,
        private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            //insert default data
            applicationScope.launch(Dispatchers.IO) {
                populateDatabase(database.get().sellDao())
            }
        }

        private suspend fun populateDatabase(sellDao: SellDao) {
            sellDao.insert(SellEntity(name = "iPhone X", price = 150000, quantity = 1, type = 2))
            sellDao.insert(SellEntity(name = "TV", price = 38000, quantity = 2, type = 2))
            sellDao.insert(SellEntity(name = "Table", price = 12000, quantity = 1, type = 2))
        }
    }
}