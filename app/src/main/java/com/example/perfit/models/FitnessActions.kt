package com.example.perfit.models

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessActions (
    @SerializedName("actionName")
    val name: String,
    @SerializedName("actionDifficulty")
    val difficulty: String,
    @SerializedName("actionDescription")
    val description: String,
    @SerializedName("actionVideo")
    val video: Uri
): Parcelable