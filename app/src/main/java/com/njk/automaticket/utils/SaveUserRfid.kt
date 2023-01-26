package com.njk.automaticket.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_RFID_NAME = "user_rfid"

class SaveUserRfid(private val context: Context){
    companion object {
        // DataStore
        val Context.userDataStore by preferencesDataStore(name = USER_RFID_NAME)
        val RFID = intPreferencesKey("RFID")
    }
    suspend fun saveRfid(rfid: String){
        context.userDataStore.edit {
            it[RFID] = rfid.toInt()
        }
    }
    // get the user's rfid
    val userRfid: Flow<Int> = context.userDataStore.data.map {
        it[RFID] ?: 0
    }
}