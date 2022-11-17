package com.njk.moveit.model

data class User(
    val rfid: Int,
    val walletBalance: Int,
    val pendingPayment: Int,
    val ticketStatus: TicketStatus,
    val tokenFcm: FcmToken,
)
enum class TicketStatus {
    VALID, INVALID
}
// TODO: Add FCM key