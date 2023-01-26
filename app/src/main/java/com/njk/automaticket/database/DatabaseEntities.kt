package com.njk.automaticket.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.njk.automaticket.domain.Bus

@Entity
data class DatabaseBus constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val distance: Double,
    val payment: Double,
    val rfid: Double,
    @ColumnInfo(name = "ticket_status") val ticketStatus: Int,
)

fun DatabaseBus.asDomainModel(): Bus {
    return Bus (
        id = id,
        distance = distance,
        payment = payment,
        rfid = rfid,
        ticketStatus = ticketStatus,
        )
}

// TODO: Figure out what to do with rfid, fcm, fb id, ( DataStore? https://medium.com/androiddevelopers/all-about-preferences-datastore-cc7995679334)