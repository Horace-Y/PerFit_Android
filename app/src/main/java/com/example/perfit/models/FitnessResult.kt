package com.example.perfit.models

import com.google.gson.annotations.SerializedName

data class FitnessResult(
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("action")
    val action: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("scores")
    val scores: HashMap<String, String>,
    @SerializedName("videoPath")
    val feedbackVideo: String
)