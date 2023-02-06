package com.njk.automaticket.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey()
    val id: Int = 0,
    @ColumnInfo(name = "first_name", defaultValue = "user")
    val firstName: String,
    @ColumnInfo(name = "mail", defaultValue = "none")
    val mail: String,
    @ColumnInfo(name = "travel_count", defaultValue = "0")
    val travelCount: Int,
    @ColumnInfo(name = "last_boarding", defaultValue = "0")
    val lastBoarding: Long,
    @ColumnInfo(name = "wallet_balance", defaultValue = "0")
    val walletBalance: Double,
    @ColumnInfo(name = "rfid_number", defaultValue = "0")
    val rfidNumber: Int,
) {
    fun getCurrency(): String {
        return NumberFormat.getInstance().format(walletBalance).toString()
    }
}

// Updated when signed in for the first time
@Entity
data class Personal(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "mail")
    val mail: String,
)

// when user model is updated
@Entity
data class Balance(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "wallet_balance")
    val walletBalance: Double,
)

// When Bus model is updated
@Entity
data class TravelStats(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "travel_count")
    val travelCount: Int = 0,
    @ColumnInfo(name = "last_boarding")
    val lastBoarding: Long,
)
// When scanning QR
@Entity
data class RfidHolder(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "rfid_number")
    val rfidNumber: Int,
)
// TODO: travelCount, lastBoarding
// TODO: formatted date