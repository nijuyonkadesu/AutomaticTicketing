package com.njk.automaticket.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.njk.automaticket.model.FcmToken
import com.njk.automaticket.model.TicketStatus
import com.njk.automaticket.model.User
import kotlin.math.abs


const val URL = "https://dummyyyyyy-aa5df-default-rtdb.asia-southeast1.firebasedatabase.app/"
const val TAG = "firebase"

class UserViewModel: ViewModel() {

    // Reference to firebase database
    private val database = Firebase.database(URL).getReference("Users")

    private fun getUserId(context: Context) {
        FirebaseInstallations.getInstance().id.addOnCompleteListener(
            OnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Log.w(TAG, "Fetching Unique ID failed", task.exception)
                    return@OnCompleteListener
                }
                context.getSharedPreferences("_", Context.MODE_PRIVATE)?.edit()?.putString("id", task.result)?.apply()
                Log.d("firebase", "new unique Token: ${task.result}")
            })
        // TODO: Use ROOM #1
    }
    private fun getFcmToken(context: Context){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val fcm = FcmToken(task.result)
                val user = createUser(fcm)
                val id = context.getSharedPreferences("_", Context.MODE_PRIVATE)?.getString("id", "fail")!!

                database.child(id).setValue(user)
                context.getSharedPreferences("_", FirebaseMessagingService.MODE_PRIVATE)?.edit()?.putString("fcm", task.result)?.apply()
                Log.d("firebase", "new FCM token: ${task.result}")
            })
    }
    // TODO: Use ROOM #2
    // TODO: + Domain layer + Fallback Offline Data #3
    private fun createUser(fcm: FcmToken): User {
        return User(
            212333433, // TODO: Get rfid from BarcodeScanning Activity
            1000,
            100,
            TicketStatus.INVALID,
            fcm,
            20, // 0 because, this function is ran just once when user installs the app
            100,
        )
    }

    // Creates a new node in firebase realtime database
    fun createNewFirebaseUser(context: Context){
        getUserId(context)
        getFcmToken(context)
    }
    fun initiatePayment(context: Context) {
        val id = context.getSharedPreferences("_", Context.MODE_PRIVATE)?.getString("id", "fail")!!

        // data snapshot of User
        database.child(id).get().addOnSuccessListener {
            val user = it.getValue<User>()!!
//            Toast.makeText(context, user?.tokenFcm?.fcm ?: "fail", Toast.LENGTH_SHORT).show()

            /* PAYMENT LOGIC START */

            val distance = abs(user.startDestination - user.endDestination)

            // Magic mathematical ratio to calculate, used with distance
            val magicNumber = 10
            val pendingPaymentCalculated = user.pendingPayment + distance * magicNumber
            //                                 ^ accumulation of old pending payment

            Toast.makeText(context, "$pendingPaymentCalculated", Toast.LENGTH_SHORT).show()
            if (user.walletBalance >= pendingPaymentCalculated) {
                user.apply {
                    walletBalance -= pendingPaymentCalculated
                    pendingPayment = 0
                }
            } else {
                user.apply {
                    pendingPayment = pendingPaymentCalculated
                }
            }

            /* PAYMENT LOGIC END */

            // make hash map, and update to database
            val userValues = user.toMap()
            val childUpdates = hashMapOf<String, Any>(
                "/$id/" to userValues
            )

            // update database
            database.updateChildren(childUpdates)

        }.addOnFailureListener {
            Toast.makeText(context, "Error: Please Try again!", Toast.LENGTH_SHORT).show()
        }

    }
}
/*
* Payment flow:
* distance = end - start (difference, not subtraction)
*
* pendingPayment = distance x magic number
*  - if balance < pendingPayment
*    >- update pendingPayment
*  - else
*    >- balance = balance - pendingPayment
*    >- pendingPayment is 0 onSuccess
*
* 1. generate pending payment
* 2. if possible deduct balance
*
* */

/*
* get user ID snapshot
*
* */

// TODO: identify account using RFID - CREATE INDEX - Perform isExist()