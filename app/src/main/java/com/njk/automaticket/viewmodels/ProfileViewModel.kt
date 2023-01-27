package com.njk.automaticket.viewmodels

import androidx.lifecycle.*
import com.njk.automaticket.data.Profile
import com.njk.automaticket.data.ProfileDao

class ProfileViewModel(private val profileDao: ProfileDao): ViewModel() {
    val fullProfile = profileDao.getProfile(1).asLiveData()
}

class ProfileViewModelFactory(private val profileDao: ProfileDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(profileDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}