package com.example.perfit.models

import com.google.gson.annotations.SerializedName

data class FitnessResult(
    @SerializedName("dateTime")
    val dateTime: String,
    @SerializedName("action")
    val action: FitnessActions,
    @SerializedName("score")
    val score: Int,
    @SerializedName("videoPath")
    val video: String
)