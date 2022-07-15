package com.example.perfit.utils

import android.net.Uri
import com.example.perfit.BuildConfig
import com.example.perfit.R
import com.example.perfit.models.FitnessActions

class Constants {
    companion object{

        const val RECORDING_DURATION_LIMIT = 5

        // Network
        const val BASE_URL = "http://localhost:5000"
        const val KEY_ID = "timestamp"
        const val KEY_MODE = "mode"
        const val KEY_VIDEO = "video"

        // Fitness actions
        const val HARD = "Hard"
        const val MEDIUM = "Medium"
        const val EASY = "Easy"
        const val ACTION1 = "Push-Up"
        const val ACTION2 = "Sit-Up"
        const val ACTION3 = "Squat"
        const val ACTION4 = "Pull-Up"
        const val ACTION5 = "Plank"
        const val ACTION6 = "Jumping Jack"
        const val DESC1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        val URI1 = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.basketball)

        val FITNESS_ACTIONS = listOf(FitnessActions(ACTION1, EASY, DESC1, URI1),
            FitnessActions(ACTION2, MEDIUM, DESC1, URI1),
            FitnessActions(ACTION3, HARD, DESC1, URI1),
            FitnessActions(ACTION4, EASY, DESC1, URI1),
            FitnessActions(ACTION5, MEDIUM, DESC1, URI1),
            FitnessActions(ACTION6, HARD, DESC1, URI1))
    }
}