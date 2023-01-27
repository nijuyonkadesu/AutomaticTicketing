package com.njk.automaticket

import android.app.Application
import com.njk.automaticket.data.ProfileDatabase

class TicketApplication: Application() {
    val profileDb: ProfileDatabase by lazy { ProfileDatabase.getDatabase(this) }
}