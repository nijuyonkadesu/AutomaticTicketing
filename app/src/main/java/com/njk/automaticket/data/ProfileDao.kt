package com.njk.automaticket.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Upsert(entity = Profile::class)
    suspend fun insertName(personal: Personal)

    @Upsert(entity = Profile::class)
    suspend fun updateWalletBalance(balance: Balance)

    @Upsert(entity = Profile::class)
    suspend fun updateTravelStats(travelStats: TravelStats)

    @Upsert(entity = Profile::class)
    suspend fun updateRfid(rfid: RfidHolder)

    @Query("select * from profile where id=:id")
    fun getProfile(id: Int): Flow<Profile>
}