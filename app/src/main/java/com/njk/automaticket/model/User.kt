package com.njk.automaticket.model

data class User(
    var rfid: Int = 0,
    var walletBalance: Int = 0,
    var pendingPayment: Int = 0,
    var ticketStatus: TicketStatus = TicketStatus.INVALID,
    var tokenFcm: FcmToken = FcmToken("zero"),
    var startDestination: Int = 0,
    var endDestination: Int = 0,
)
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