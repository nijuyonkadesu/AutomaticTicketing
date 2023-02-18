package com.njk.automaticket.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.njk.automaticket.BuildConfig
import com.njk.automaticket.data.ProfileDao
import com.njk.automaticket.data.TravelStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Count(
    var travelCount: Int = 0,
)
@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val profileDao: ProfileDao
    ): ViewModel() {
    val fullProfile = profileDao.getProfile(1).asLiveData()
    private var count: MutableLiveData<Count> = MutableLiveData()
    fun updateTravelStats(): LiveData<Count>{
        val seatsDatabase = Firebase.database(URL)
            .getReference("Users").child("CountSeats")

            val seatsCountListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        count.postValue(dataSnapshot.getValue<Count>())
                        // TODO: time conversion
                        if(BuildConfig.DEBUG)
                            Log.d(TAG, "seats update received")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        TAG, "loadPost:onCancelled", error.toException()
                    )
                }
            }
        seatsDatabase.addValueEventListener(seatsCountListener)
        CoroutineScope(Dispatchers.IO).launch {
            profileDao.updateTravelStats(TravelStats(
                1,
                count.value?.travelCount ?: 5,
                1674813614,
            ))
        }
        return count
        // TODO fix count not being observed
    }
}