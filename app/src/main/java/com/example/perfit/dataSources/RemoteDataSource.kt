package com.example.perfit.dataSources

import com.example.perfit.dataSources.network.FitnessApi
import com.example.perfit.models.FitnessFeedback
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val fitnessApi: FitnessApi
) {
    suspend fun sendRecording(data: Map<String, String>): Response<FitnessFeedback> {
        return fitnessApi.sendRecording(data)
    }
}