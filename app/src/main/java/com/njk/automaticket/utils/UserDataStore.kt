package com.njk.automaticket.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val USER_RFID_NAME = "user_rfid"

class UserDataStore(private val context: Context){
    companion object {
        // DataStore
        val Context.userDataStore by preferencesDataStore(name = USER_RFID_NAME)
        val RFID = intPreferencesKey("RFID")
        val ID = stringPreferencesKey("ID")
        val FCM = stringPreferencesKey("FCM")
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
    suspend fun saveId(id: String){
        context.userDataStore.edit {
            it[ID] = id
        }
    }
    suspend fun getId(): String{
        return context.userDataStore.data.first()[ID] ?: "0"
    }
    suspend fun saveFcm(fcmToken: String){
        context.userDataStore.edit {
            it[FCM] = fcmToken
        }
    }
    suspend fun getFcm(): String{
        return context.userDataStore.data.first()[FCM] ?: "0"
    }
}