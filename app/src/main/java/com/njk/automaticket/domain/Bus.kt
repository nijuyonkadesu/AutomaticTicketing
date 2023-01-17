package com.njk.automaticket.domain

data class Bus(
    var id: Int,
    var distance: Double = 0.0,
    var payment: Double = 0.0,
    var rfid: Double = 0.0,
    var ticketStatus: Int = 0,
//    val balance: Double,
)
