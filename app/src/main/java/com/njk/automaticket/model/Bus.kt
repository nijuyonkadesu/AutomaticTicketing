package com.njk.automaticket.model

data class Bus(
    var distance: Double = 0.0,
    var payment: Double = 0.0,
    var rfid: Double = 0.0,
    var ticket_status: Int = 0,
//    val balance: Double,
)
