package com.njk.automaticket.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.njk.automaticket.database.getDatabase
import com.njk.automaticket.domain.Bus
import com.njk.automaticket.repository.BusRepository
import kotlinx.coroutines.launch
import java.io.IOException

class BusViewModel(application: Application): ViewModel() {

    private val busRepository = BusRepository(getDatabase(application))
    private val bus: LiveData<Bus> = busRepository.bus.asLiveData()

    private fun refreshBusDataFromRepository(){
        viewModelScope.launch {
            try {
                busRepository.refreshBus()
            }
            catch (networkError: IOException){
                Log.e(TAG,"Turn on Internet: $networkError")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class Factory(val app: Application): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BusViewModel::class.java)) {
            return BusViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct view model")
    }
}