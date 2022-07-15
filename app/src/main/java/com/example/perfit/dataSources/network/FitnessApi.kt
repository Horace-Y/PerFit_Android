package com.example.perfit.dataSources.network

import com.example.perfit.models.FitnessResult
import retrofit2.Response
import retrofit2.http.*

interface FitnessApi {

    @Headers
    @POST("/feedback")
    suspend fun sendRecording(
        @Body queries: Map<String, String>
    ): Response<FitnessResult>

//    @GET("")
//    suspend fun getFeedback(
//        @QueryMap searchQuery: Map<String, String>
//    ): Response<FitnessResult>
}