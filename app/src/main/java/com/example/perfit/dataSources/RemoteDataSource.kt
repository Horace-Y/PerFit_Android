package com.example.perfit.dataSources

import com.example.perfit.dataSources.network.FitnessApi
import com.example.perfit.models.FitnessResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val fitnessApi: FitnessApi
){
    suspend fun sendRecording(queries: Map<String, String>): Response<FitnessResult> {
        return fitnessApi.sendRecording(queries)
    }

    suspend fun getFeedback(searchQuery: Map<String, String>): Response<FitnessResult> {
        return fitnessApi.getFeedback(searchQuery)
    }
}