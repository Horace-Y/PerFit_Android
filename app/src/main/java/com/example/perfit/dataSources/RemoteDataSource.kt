package com.example.perfit.dataSources

import android.util.Log
import com.example.perfit.dataSources.network.FitnessApi
import com.example.perfit.models.FitnessResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val fitnessApi: FitnessApi
) {
    suspend fun sendRecording(data: Map<String, String>): Response<FitnessResult> {
        for ((key, value) in data) {
            Log.d("Map", "$key $value")
        }
        return fitnessApi.sendRecording(data)
    }
}