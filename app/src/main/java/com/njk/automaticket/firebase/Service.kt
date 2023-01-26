package com.njk.automaticket.firebase

//interface BusService {
//    suspend fun getNetworkBus(): NetworkBus
//}

//    return NetworkBus(
//        id = 1,
//        distance = 100.0,
//        payment = 100.0,
//        rfid = 2000.0,
//        ticketStatus = 10
//    )

//suspend fun getNetworkBus(): NetworkBus {
//    // TODO: .getReference(Bus)
//    val busDatabase =
//        Firebase.database("https://busticketsystem-f2ca3-default-rtdb.asia-southeast1.firebasedatabase.app/")
//            .getReference("Bus")
//
//    val networkBusListener = object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            val bus = dataSnapshot.getValue<NetworkBus>()!!
//        }
//        override fun onCancelled(error: DatabaseError) {
//            Log.w(com.njk.automaticket.viewmodels.TAG, "loadPost:onCancelled", error.toException())
//        }
//    }
//    busDatabase.addValueEventListener(networkBusListener)
//    return bus
//}