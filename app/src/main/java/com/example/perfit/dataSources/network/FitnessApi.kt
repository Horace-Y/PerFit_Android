package com.example.perfit.dataSources.network

import com.example.perfit.models.FitnessResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface FitnessApi {

    @POST("/feedback")
    suspend fun sendRecording(
        @QueryMap queries: Map<String, String>
    ): Response<FitnessResult>

    @GET("")
    suspend fun getFeedback(
        @QueryMap searchQuery: Map<String, String>
    ): Response<FitnessResult>
}