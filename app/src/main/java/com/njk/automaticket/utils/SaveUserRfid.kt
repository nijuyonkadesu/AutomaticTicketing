package com.njk.automaticket.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

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
    suspend fun getRfid(): Int{
        return context.userDataStore.data.first()[RFID] ?: 0
    }
}