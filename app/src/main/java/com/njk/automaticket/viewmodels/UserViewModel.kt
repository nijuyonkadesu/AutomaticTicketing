package com.njk.moveit.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.njk.moveit.model.Token
import com.njk.moveit.model.User

const val URL = "https://busticketsystem-f2ca3-default-rtdb.asia-southeast1.firebasedatabase.app/"
const val TAG = "firebase"

class UserViewModel: ViewModel() {

//    private val database = FirebaseDatabase.getInstance(URL).getReference("Users")
    private val database = Firebase.database(URL).getReference("Users")
    private fun firebaseFcmToken(context: Context, user: User){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                context.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)?.edit()?.putString("fcm", task.result)?.apply()
                Log.d("firebase", "new FCM token: ${task.result}")
            })
    }

    fun firebaseId(context: Context, user: User){
        FirebaseInstallations.getInstance().id.addOnCompleteListener(
            OnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Log.w(TAG, "Fetching Unique ID failed", task.exception)
                    return@OnCompleteListener
                }
                val token = Token(task.result, user)
                database.child(task.result).setValue(token)
                context.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)?.edit()?.putString("id", task.result)?.apply()
                Log.d("firebase", "new unique Token: ${task.result}")
            })
    }
}