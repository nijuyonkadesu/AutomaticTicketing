package com.njk.automaticket

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.njk.automaticket.data.ProfileDatabase

class TicketApplication: Application() {
    val profileDb: ProfileDatabase by lazy { ProfileDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}