package com.example.perfit.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
): AndroidViewModel(application){

    /** RETROFIT */
    val recordingResponse: MutableLiveData<NetworkResult<FitnessResult>> = MutableLiveData()
//    val searchedRecipesResponse: MutableLiveData<NetworkResult<FitnessResult>> = MutableLiveData()

    fun sendRecording(queries: Map<String, String>) = viewModelScope.launch {
        sendRecordingToServer(queries)
    }

//    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
//        searchRecipesSafeCall(searchQuery)
//    }

    private suspend fun sendRecordingToServer(queries: Map<String, String>) {
        recordingResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try {
                val response = repository.remote.sendRecording(queries)
                recordingResponse.value = handleServerResponse(response)

//                val responseData = recipesResponse.value!!.data
//                if(responseData != null){
//                    offlineCacheResult(responseData)
//                }
            }catch (e: Exception){
                recordingResponse.value = NetworkResult.Error("Response Not Received.")
            }
        }else{
            recordingResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

//    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
//        searchedRecipesResponse.value = NetworkResult.Loading()
//        if(hasInternetConnection()){
//            try {
//                val response = repository.remote.getRecording(searchQuery)
//                searchedRecipesResponse.value = handleFoodRecipesResponse(response)
//            }catch (e: Exception){
//                searchedRecipesResponse.value = NetworkResult.Error("Recipes Not Found.")
//            }
//        }else{
//            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
//        }
//    }

    private fun handleServerResponse(response: Response<FitnessResult>): NetworkResult<FitnessResult> {
        return when{
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return true
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}