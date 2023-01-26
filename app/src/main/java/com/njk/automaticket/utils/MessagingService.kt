package com.njk.automaticket.utils

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Message", message.data.toString())
        super.onMessageReceived(message)
    }
}