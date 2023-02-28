package com.example.perfit.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessFeedback(
    @SerializedName("total")
    val total: Int,
    @SerializedName("scores")
    val scores: HashMap<String, String>,
    @SerializedName("video")
    val video: String,
): Parcelable