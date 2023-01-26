package com.njk.automaticket.firebase

import com.njk.automaticket.database.DatabaseBus

data class NetworkBus(
    var id: Int = 0,
    var distance: Double = 0.0,
    var payment: Double = 0.0,
    var rfid: Double = 0.0,
    var ticketStatus: Int = 0,
)

fun NetworkBus.asDatabase(): DatabaseBus {
    return DatabaseBus(
        id = id,
        distance = distance,
        payment = payment,
        rfid = rfid,
        ticketStatus = ticketStatus,
    )
}