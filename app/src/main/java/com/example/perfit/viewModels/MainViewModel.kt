package com.example.perfit.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.perfit.dataSources.Repository
import com.example.perfit.dataSources.database.FitnessResultEntity
import com.example.perfit.models.FitnessActions
import com.example.perfit.models.FitnessFeedback
import com.example.perfit.models.FitnessResult
import com.example.perfit.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** RETROFIT */
    val sendRecordingResponse: MutableLiveData<NetworkResult<FitnessFeedback>> = MutableLiveData()

    fun sendRecording(recordingData: Map<String, String>) = viewModelScope.launch {
        sendRecordingToServer(recordingData)
    }

    private suspend fun sendRecordingToServer(recordingData: Map<String, String>) {
        sendRecordingResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.sendRecording(recordingData)
                sendRecordingResponse.value = handleServerResponse(response)
            } catch (e: Exception) {
                sendRecordingResponse.value = NetworkResult.Error("Server Response Not Received.")
            }
        } else {
            sendRecordingResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleServerResponse(response: Response<FitnessFeedback>): NetworkResult<FitnessFeedback> {
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

    /** ROOM DATABASE */
    val readResults: LiveData<List<FitnessResultEntity>> = repository.local.readResults().asLiveData()

    fun offlineCacheResult(fitnessActions: FitnessActions, score: Int, video: String){
        val fitnessResult = FitnessResult(SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US).format(System.currentTimeMillis()), fitnessActions, score, video)
        val fitnessResultEntity = FitnessResultEntity(fitnessResult)
        addNewResult(fitnessResultEntity)
    }

    private fun addNewResult(fitnessResultEntity: FitnessResultEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.addNewResult(fitnessResultEntity)
        }

    private fun deleteLocalResult(fitnessResultEntity: FitnessResultEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteResult(fitnessResultEntity)
        }
}