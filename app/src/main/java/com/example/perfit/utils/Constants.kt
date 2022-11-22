package com.example.perfit.utils

import android.net.Uri
import com.example.perfit.BuildConfig
import com.example.perfit.R
import com.example.perfit.models.FitnessActions
import com.example.perfit.models.FitnessFeedback
import com.example.perfit.models.FitnessResult

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
        private val URI1 = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.basketball)

        val FITNESS_ACTIONS = listOf(FitnessActions(ACTION1, EASY, DESC1, URI1),
            FitnessActions(ACTION2, MEDIUM, DESC1, URI1),
            FitnessActions(ACTION3, HARD, DESC1, URI1),
            FitnessActions(ACTION4, EASY, DESC1, URI1),
            FitnessActions(ACTION5, MEDIUM, DESC1, URI1),
            FitnessActions(ACTION6, HARD, DESC1, URI1))

        private val FITNESS_FEEDBACK1 = FitnessFeedback(100, "aaa")
        private val FITNESS_FEEDBACK2 = FitnessFeedback(90, "aaa")
        private val FITNESS_FEEDBACK3 = FitnessFeedback(80, "aaa")
        private val FITNESS_FEEDBACK4 = FitnessFeedback(60, "aaa")
        private val FITNESS_FEEDBACK5 = FitnessFeedback(70, "aaa")
        private val FITNESS_FEEDBACK6 = FitnessFeedback(50, "aaa")

        val FITNESS_RESULTS = listOf(FitnessResult("2022-09-01", FitnessActions(ACTION1, EASY, DESC1, URI1), FITNESS_FEEDBACK1),
            FitnessResult("2022-09-02", FitnessActions(ACTION2, MEDIUM, DESC1, URI1), FITNESS_FEEDBACK2),
            FitnessResult("2022-09-03", FitnessActions(ACTION3, HARD, DESC1, URI1), FITNESS_FEEDBACK3),
            FitnessResult("2022-09-04", FitnessActions(ACTION4, EASY, DESC1, URI1), FITNESS_FEEDBACK4),
            FitnessResult("2022-09-05", FitnessActions(ACTION5, MEDIUM, DESC1, URI1), FITNESS_FEEDBACK5),
            FitnessResult("2022-09-06", FitnessActions(ACTION6, HARD, DESC1, URI1), FITNESS_FEEDBACK6))
    }
}