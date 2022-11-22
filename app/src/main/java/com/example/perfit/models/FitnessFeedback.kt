package com.example.perfit.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessFeedback(
    @SerializedName("score")
    val score: Int,
    @SerializedName("video")
    val video: String,
): Parcelable