package com.njk.automaticket.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.njk.automaticket.BuildConfig
import com.njk.automaticket.model.Bus
import com.njk.automaticket.utils.UserDataStore

class BusViewModel : ViewModel() {
    private var bus: MutableLiveData<Bus> = MutableLiveData()

    suspend fun getBusDetails(context: Context): LiveData<Bus> {
        val userDataStore = UserDataStore(context) // .child("1395101461")
        val busDatabase = Firebase.database(URL).getReference("Users").child(userDataStore.run {
            getRfid().toString()
        })

        val networkBusListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    bus.postValue(dataSnapshot.getValue<Bus>())
                    if(BuildConfig.DEBUG)
                        Log.d(TAG, "update received")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    TAG, "loadPost:onCancelled", error.toException()
                )
            }
        }
        busDatabase.addValueEventListener(networkBusListener)
        return bus
    }
    init {
        Firebase.database.setPersistenceEnabled(true)
        // Enables disk caching, to show data in the absence of network
    }
    // Connection Status https://firebase.google.com/docs/database/android/offline-capabilities
}