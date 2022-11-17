package com.njk.automaticket.model

data class User(
    val rfid: Int,
    val walletBalance: Int,
    val pendingPayment: Int,
    val ticketStatus: TicketStatus,
    val tokenFcm: FcmToken,
    val startDestination: Int,
    val endDestination: Int,
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