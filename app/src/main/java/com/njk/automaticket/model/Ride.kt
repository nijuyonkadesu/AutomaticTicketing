package com.njk.automaticket.model

import androidx.annotation.DrawableRes
import kotlinx.datetime.LocalDateTime

data class Ride (
    val day: String,
    val from: String,
    val to: String,
    val price: String,
    val userRating: String,
    @DrawableRes val mapArt: Int,
)