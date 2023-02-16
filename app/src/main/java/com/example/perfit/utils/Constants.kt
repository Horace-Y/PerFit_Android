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
//        private val URI1 = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.basketball
        private val URI_PUSH_UP = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.push_up_demo
        private val URI_SIT_UP = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.sit_up_demo
        private val URI_SQUAT = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.squat_demo
        private val URI_PULL_UP = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.pull_up_demo
        private val URI_PLANK = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.plank_demo
        private val URI_JUMPING_JACK = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.jumping_jack_demo

        val FITNESS_ACTIONS = listOf(FitnessActions(ACTION1, EASY, DESC1, URI_PUSH_UP),
            FitnessActions(ACTION2, MEDIUM, DESC1, URI_SIT_UP),
            FitnessActions(ACTION3, HARD, DESC1, URI_SQUAT),
            FitnessActions(ACTION4, EASY, DESC1, URI_PULL_UP),
            FitnessActions(ACTION5, MEDIUM, DESC1, URI_PLANK),
            FitnessActions(ACTION6, HARD, DESC1, URI_JUMPING_JACK))

        // ROOM Database
        const val DATABASE_NAME = "fitness_results_database"
        const val FITNESS_RESULTS_TABLE = "fitness_results_table"
    }
}