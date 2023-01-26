package com.njk.automaticket.domain

import com.google.firebase.database.Exclude

data class Bus(
    var id: Int = 0,
    var distance: Double = 0.0,
    var payment: Double = 0.0,
    var rfid: Double = 0.0,
    var ticketStatus: Int = 0,
//    val balance: Double,
) {
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "id" to id,
            "distance" to distance,
            "payment" to payment,
            "rfid" to rfid,
            "ticketStatus" to ticketStatus,
        )
    }
}
