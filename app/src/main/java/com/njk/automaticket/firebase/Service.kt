package com.njk.automaticket.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


lateinit var bus: NetworkBus

suspend fun getNetworkBus(): NetworkBus {
    // TODO: .getReference(Bus)
    val busDatabase =
        Firebase.database("https://busticketsystem-f2ca3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")

    val networkBusListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            bus = dataSnapshot.getValue<NetworkBus>()!!
        }
        override fun onCancelled(error: DatabaseError) {
            Log.w(com.njk.automaticket.viewmodels.TAG, "loadPost:onCancelled", error.toException())
        }
    }
    busDatabase.addValueEventListener(networkBusListener)
    return bus
}
