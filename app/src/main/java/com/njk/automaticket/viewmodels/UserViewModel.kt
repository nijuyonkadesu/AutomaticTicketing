package com.njk.automaticket.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.njk.automaticket.BuildConfig
import com.njk.automaticket.data.Balance
import com.njk.automaticket.data.ProfileDao
import com.njk.automaticket.model.FcmToken
import com.njk.automaticket.model.TicketStatus
import com.njk.automaticket.model.User
import com.njk.automaticket.utils.UserDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs


const val URL = "https://dummyyyyyy-aa5df-default-rtdb.asia-southeast1.firebasedatabase.app/"
const val TAG = "firebase"

@HiltViewModel
class UserViewModel @Inject constructor(
    val profileDao: ProfileDao,
    val userDataStore: UserDataStore,
): ViewModel() {

    // Reference to firebase database
    private val database = Firebase.database(URL).getReference("Users")

    private suspend fun getUserId(context: Context) {
        FirebaseInstallations.getInstance().id.addOnCompleteListener(
            OnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Log.w(TAG, "Fetching Unique ID failed", task.exception)
                    return@OnCompleteListener
                }
                viewModelScope.launch {
                    userDataStore.saveId(task.result)
                    if(BuildConfig.DEBUG)
                        Log.d("firebase", "new unique Token: ${task.result}")
                }
            })
    }
    private suspend fun getFcmToken(context: Context){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                viewModelScope.launch {
                    userDataStore.saveFcm(task.result)

                    val fcm = FcmToken(userDataStore.getFcm())
                    database.child(userDataStore.getId()).setValue(createUser(fcm))
                    if(BuildConfig.DEBUG)
                        Log.d("firebase", "new FCM token: ${task.result}")
                }
            })
    }
    private suspend fun createUser(fcm: FcmToken): User {
        if(BuildConfig.DEBUG)
            Log.d(TAG, userDataStore.getRfid().toString())
        return User(
            userDataStore.getRfid(),
            1500.0,
            20.0,
            TicketStatus.INVALID,
            fcm,
            20, // 0 because, this function is ran just once when user installs the app
            100,
        )
    }

    // Creates a new node in firebase realtime database
    fun createNewFirebaseUser(context: Context){
        viewModelScope.launch {
            getUserId(context)
            getFcmToken(context)
        }
    }
    fun initiatePayment(context: Context) {
        viewModelScope.launch {
            val id = userDataStore.getId()
            database.child(id).get().addOnSuccessListener {
                val user = it.getValue<User>()!!
//            Toast.makeText(context, user?.tokenFcm?.fcm ?: "fail", Toast.LENGTH_SHORT).show()

                /* PAYMENT LOGIC START */

                val distance = abs(user.startDestination - user.endDestination)

                // Magic mathematical ratio to calculate, used with distance
                val magicNumber = 0.3
                val pendingPaymentCalculated = user.pendingPayment + distance * magicNumber
                //                                 ^ accumulation of old pending payment

                Toast.makeText(context, "$pendingPaymentCalculated", Toast.LENGTH_SHORT).show()
                if (user.walletBalance >= pendingPaymentCalculated) {
                    user.apply {
                        walletBalance -= pendingPaymentCalculated
                        pendingPayment = 0.0
                    }
                } else {
                    user.apply {
                        pendingPayment = pendingPaymentCalculated
                    }
                }

                /* PAYMENT LOGIC END */
                CoroutineScope(Dispatchers.IO).launch {
                    profileDao.updateWalletBalance(Balance(
                        1,
                        walletBalance = user.walletBalance
                    ))
                }

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