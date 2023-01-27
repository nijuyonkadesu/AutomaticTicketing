package com.njk.automaticket.model

import com.google.firebase.database.Exclude

data class User(
    var rfid: Int = 0,
    var walletBalance: Double = 0.0,
    var pendingPayment: Double = 0.0,
    var ticketStatus: TicketStatus = TicketStatus.INVALID,
    var tokenFcm: FcmToken = FcmToken("zero"),
    var startDestination: Int = 0,
    var endDestination: Int = 0,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "rfid" to rfid,
            "walletBalance" to walletBalance,
            "pendingPayment" to pendingPayment,
            "ticketStatus" to ticketStatus,
            "tokenFcm" to tokenFcm,
            "startDestination" to startDestination,
            "endDestination" to endDestination,
        )
    }
}
enum class TicketStatus {
    VALID, INVALID
}
/*
* rfid - given by nodemcu
* walletBalance - amount of balance present in your account
* pendingPayment - calculated based on the distance travelled
* ticketStatus - States if your in bus / not
* startDestination - ( one dimensional )
* endDestination - ( one dimensional )
* tokenFcm - Firebase Cloud Messaging unique token to send targeted notification
*
*  > A user is identified by their unique id
* */