package com.njk.automaticket.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.njk.automaticket.R
import com.njk.automaticket.database.getDatabase
import com.njk.automaticket.domain.Bus
import com.njk.automaticket.repository.BusRepository
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.NumberFormat

class BusViewModel(application: Application): ViewModel() {

    private val busRepository = BusRepository(getDatabase(application))
    val bus: LiveData<Bus> = busRepository.bus.asLiveData()

    fun getPayment(): String {
        return "Payment" + (NumberFormat.getInstance().format(bus.value?.payment) ?: "0")
    }

    fun getDistance(): String {
        return "Distance: " + bus.value?.distance.toString()
    }

    init {
        refreshBusDataFromRepository()
    }

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
    @Suppress("UNCHECKED_CAST")
    class Factory(val app: Application): ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(BusViewModel::class.java)) {
                return BusViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }
}
