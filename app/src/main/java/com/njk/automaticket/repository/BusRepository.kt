package com.njk.automaticket.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.njk.automaticket.database.BusDatabase
import com.njk.automaticket.database.DatabaseBus
import com.njk.automaticket.database.asDomainModel
import com.njk.automaticket.domain.Bus
import com.njk.automaticket.firebase.NetworkBus
import com.njk.automaticket.firebase.asDatabase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BusRepository(private val database: BusDatabase) {
    suspend fun refreshBus() {
//        CoroutineScope(Dispatchers.IO).launch {
//            database.busDao.insertBus(
//                DatabaseBus(
//                    id = 1,
//                    distance = 100.0,
//                    payment = 100.0,
//                    rfid = 2000.0,
//                    ticketStatus = 10
//                )
//            )
//        }
        val handler = CoroutineExceptionHandler { _, throwable ->
            println("firebase failure: $throwable")
        }
        CoroutineScope(Dispatchers.IO + handler).launch {
            val busDatabase =
                Firebase.database("https://dummyyyyyy-aa5df-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Bus")

            val networkBusListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val bus = dataSnapshot.getValue<NetworkBus>()!!
                    CoroutineScope(Dispatchers.IO).launch {
                        database.busDao.insertBus(bus.asDatabase())
                    } // TODO: fix occational locked db
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(
                        com.njk.automaticket.viewmodels.TAG,
                        "loadPost:onCancelled",
                        error.toException()
                    )
                }
            }
            busDatabase.addValueEventListener(networkBusListener)
        }
    }
    val bus: Flow<Bus> = database.busDao.getBus().map {
        it.asDomainModel()
    }
}
// TODO: Modularize Home Fragment