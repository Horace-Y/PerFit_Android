package com.example.perfit.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.example.perfit.dataSources.Repository
import com.example.perfit.models.FitnessResult
import com.example.perfit.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** RETROFIT */
    val sendRecordingResponse: MutableLiveData<NetworkResult<FitnessResult>> = MutableLiveData()

    fun sendRecording(data: Map<String, String>) = viewModelScope.launch {
        sendRecordingToServer(data)
    }

    private suspend fun sendRecordingToServer(data: Map<String, String>) {
        sendRecordingResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.sendRecording(data)
                sendRecordingResponse.value = handleServerResponse(response)
                // TODO: save the received feedback to local
//                val responseData = getFeedbackResponse.value!!.data
//                if(responseData != null){
//                    offlineCacheResult(responseData)
//                }
            } catch (e: Exception) {
                sendRecordingResponse.value = NetworkResult.Error("Server Response Not Received.")
            }
        } else {
            sendRecordingResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleServerResponse(response: Response<FitnessResult>): NetworkResult<FitnessResult> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Connection to Server Timeout")
            }
            response.isSuccessful -> {
                val result = response.body()
                NetworkResult.Success(result!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return true
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}