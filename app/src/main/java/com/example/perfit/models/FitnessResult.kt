package com.example.perfit.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class FitnessResult(
    @SerializedName("score")
    val score: Int,
    @SerializedName("video")
    val video: String,
): Parcelable