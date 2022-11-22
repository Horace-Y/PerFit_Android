package com.example.perfit.dataSources.network

import com.example.perfit.models.FitnessFeedback
import retrofit2.Response
import retrofit2.http.*

interface FitnessApi {
    @Headers("Content-Type: application/json")
    @POST("/feedback")
    suspend fun sendRecording(
        @Body data: Map<String, String>
    ): Response<FitnessFeedback>
}