package com.example.perfit.utils

import com.example.perfit.BuildConfig
import com.example.perfit.R
import com.example.perfit.models.FitnessActions

class Constants {
    companion object{

        const val RECORDING_DURATION_LIMIT = 5

        // Network
        const val BASE_URL = "http://10.0.2.2:5000"
        const val KEY_ID = "timestamp"
        const val KEY_MODE = "mode"
        const val KEY_VIDEO = "video"

        // Fitness actions
        private const val HARD = "Hard"
        private const val MEDIUM = "Medium"
        private const val EASY = "Easy"
        private const val ACTION1 = "Push-Up"
        private const val ACTION2 = "Sit-Up"
        private const val ACTION3 = "Squat"
        private const val ACTION4 = "Pull-Up"
        private const val ACTION5 = "Plank"
        private const val ACTION6 = "Jumping Jack"
        private const val DESC1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        private val URI1 = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.basketball

        val FITNESS_ACTIONS = listOf(FitnessActions(ACTION1, EASY, DESC1, URI1),
            FitnessActions(ACTION2, MEDIUM, DESC1, URI1),
            FitnessActions(ACTION3, HARD, DESC1, URI1),
            FitnessActions(ACTION4, EASY, DESC1, URI1),
            FitnessActions(ACTION5, MEDIUM, DESC1, URI1),
            FitnessActions(ACTION6, HARD, DESC1, URI1))

        // ROOM Database
        const val DATABASE_NAME = "fitness_results_database"
        const val FITNESS_RESULTS_TABLE = "fitness_results_table"
    }
}