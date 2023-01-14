package android.com.demo.di

import android.com.demo.data.room.AppRoomDatabase
import android.com.demo.data.room.dao.SellDao
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext app: Context,
        callback: AppRoomDatabase.Callback
    ): AppRoomDatabase {
        val db = Room.databaseBuilder(
            app,
            AppRoomDatabase::class.java,
            "app_database"
        ).addCallback(callback)
            .build()
        db.openHelper.writableDatabase//Forces an Open to trigger callback(not wait until select/insert first time)
        return db
    }

    @Singleton
    @Provides
    fun provideSellDao(db: AppRoomDatabase): SellDao {
        return db.sellDao()
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }
}
