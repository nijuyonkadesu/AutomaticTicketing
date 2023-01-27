package com.njk.automaticket.model

data class Bus(
    var onBoardDistance: Double = 0.0,
    var dropOffDistance: Double = 0.0,
    var fare: Double = 0.0,
    var ticketStatus: Int = 0,
)
