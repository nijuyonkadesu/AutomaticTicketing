package com.njk.automaticket.data

import com.njk.automaticket.R
import com.njk.automaticket.model.Ride
import com.njk.automaticket.utils.DateTimeUtil
import java.text.NumberFormat

class RideHistorySource {
    val date = DateTimeUtil
    fun getCurrency(fare: Double): String
    = NumberFormat.getCurrencyInstance().format(fare)
    fun loadRideHistory(): List<Ride>{
        return listOf(
            Ride(
                date.formatNoteDate(date.now()),
                "Bessant nagar",
                "Vellore",
                getCurrency(32.58),
                "⭐⭐⭐⭐⭐",
                R.drawable.ic_trip
            ),
            Ride(
                date.formatNoteDate(date.now()),
                "Sipcot",
                "Nandanam",
                getCurrency(50.89),
                "⭐⭐⭐⭐⭐",
                R.drawable.ic_trip
            ),
            Ride(
                date.formatNoteDate(date.now()),
                "Mylapore",
                "Mandavelli",
                getCurrency(12.38),
                "⭐⭐⭐⭐⭐",
                R.drawable.ic_trip
            ),Ride(
                date.formatNoteDate(date.now()),
                "Ashok Nagar",
                "West Mambalam",
                getCurrency(8.03),
                "⭐⭐⭐⭐⭐",
                R.drawable.ic_trip
            ),
            Ride(
                date.formatNoteDate(date.now()),
                "Ponamalli",
                "1000 Lights",
                getCurrency(17.92),
                "⭐⭐⭐⭐⭐",
                R.drawable.ic_trip
            ),
        )
    }
}
// TODO: Fetch from Database & use viewmodel
// TODO: Save ride when Ticket status toggles from 1 to 0