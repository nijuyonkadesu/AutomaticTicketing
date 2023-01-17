package com.njk.automaticket.repository

import com.njk.automaticket.database.BusDatabase
import com.njk.automaticket.database.asDomainModel
import com.njk.automaticket.domain.Bus
import com.njk.automaticket.firebase.asDatabase
import com.njk.automaticket.firebase.getNetworkBus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class BusRepository(private val database: BusDatabase) {
    suspend fun refreshBus() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            println("firebase failure: $throwable")
        }
        CoroutineScope(Dispatchers.IO + handler).launch {
            val bus = getNetworkBus()
            database.busDao.insertBus(bus.asDatabase())
        }
    }

    val bus: Flow<Bus> = database.busDao.getBus().map {
        it.asDomainModel()
    }
}
// TODO: Modularize Home Fragment