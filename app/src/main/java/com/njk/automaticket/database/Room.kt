package com.njk.automaticket.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BusDao {
    @Query("select * from databasebus")
    fun getBus(): Flow<DatabaseBus>

    @Upsert
    fun insertBus(bus: DatabaseBus)
}

@Database(entities = [DatabaseBus::class], version = 1)
abstract class BusDatabase: RoomDatabase() {
    abstract val busDao: BusDao
}

private lateinit var INSTANCE: BusDatabase

fun getDatabase(context: Context): BusDatabase{
    synchronized(BusDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                BusDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}