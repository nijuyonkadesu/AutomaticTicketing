package com.njk.automaticket.database

import androidx.room.Entity

@Entity
data class DatabaseBus constructor(
    val distance: Double,
    val payment: Double,
    val rfid: Double,
    val ticket_status: Int,
)

// TODO: Figure out what to do with rfid, fcm, fb id, ( DataStore? https://medium.com/androiddevelopers/all-about-preferences-datastore-cc7995679334)