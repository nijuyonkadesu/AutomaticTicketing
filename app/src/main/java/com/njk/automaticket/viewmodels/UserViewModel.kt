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
import com.njk.automaticket.model.TicketStatus
import com.njk.automaticket.model.User


const val URL = "https://busticketsystem-f2ca3-default-rtdb.asia-southeast1.firebasedatabase.app/"
const val TAG = "firebase"

class UserViewModel: ViewModel() {

    // Reference to firebase database
    private val database = Firebase.database(URL).getReference("Users")

    fun getUserId(context: Context) {
        FirebaseInstallations.getInstance().id.addOnCompleteListener(
            OnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Log.w(TAG, "Fetching Unique ID failed", task.exception)
                    return@OnCompleteListener
                }
                context.getSharedPreferences("_", Context.MODE_PRIVATE)?.edit()?.putString("id", task.result)?.apply()
                Log.d("firebase", "new unique Token: ${task.result}")
            })
    }
    fun getFcmToken(context: Context){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val fcm = FcmToken(task.result)
                val user = createUser(fcm)
                val id = context?.getSharedPreferences("_", Context.MODE_PRIVATE)?.getString("id", "fail")!!

                database.child(id).setValue(user)
                context?.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)?.edit()?.putString("fcm", task.result)?.apply()
                Log.d("firebase", "new FCM token: ${task.result}")
            })
    }
    private fun createUser(fcm: FcmToken): User {
        return User(
            8787, // TODO: Get rfid from BarcodeScanning Activity
            1000,
            0, // TODO: Calculate from distance, accumulate, subtract, reset
            TicketStatus.INVALID,
            fcm,
            0, // 0 because, this function is ran just once when user installs the app
            0,
        )
    }

    // Creates a new node in firebase realtime database
    fun createNewFirebaseUser(context: Context){
        getUserId(context)
        getFcmToken(context)
    }
}