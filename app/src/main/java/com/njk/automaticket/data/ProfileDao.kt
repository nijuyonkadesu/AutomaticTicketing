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

    @Query("select * from profile where id=:id")
    fun getProfile(id: Int): Flow<Profile>
}