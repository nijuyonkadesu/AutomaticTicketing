package com.njk.moveit.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.njk.automaticket.model.FcmToken
import com.njk.automaticket.model.User


const val URL = "https://busticketsystem-f2ca3-default-rtdb.asia-southeast1.firebasedatabase.app/"
const val TAG = "firebase"

class UserViewModel: ViewModel() {

}