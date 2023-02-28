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
        private const val ACTION_PUSH_UP = "Push-Up"
        private const val ACTION_SIT_UP = "Sit-Up"
        private const val ACTION_SQUAT = "Squat"
        private const val ACTION_PULL_UP = "Pull-Up"
        private const val ACTION_PLANK = "Plank"
        private const val ACTION_JUMPING_JACK = "Jumping-Jack"

        private const val DESC_PUSH_UP = "Push-up is a common strength training exercise that mainly targets the muscles of the chest, shoulders, and triceps."
        private const val DESC_SIT_UP = "Sit up is an exercise that involves lying on one's back with knees bent and feet flat on the ground, then lifting the upper body off the ground by contracting the abdominal muscles."
        private const val DESC_SQUAT = "Squat is a lower body exercise that primarily targets the muscles of the hips, thighs, and glutes."
        private const val DESC_PULL_UP = "A pull-up is a strength-training exercise that involves lifting oneself up using an overhead bar while hanging with arms fully extended."
        private const val DESC_PLANK = "Plank is a bodyweight exercise that strengthens the core muscles, including the abs, back, and hips."
        private const val DESC_JUMPING_JACK = "Jumping jack is a full-body exercise that involves jumping and spreading both arms and legs to the side at the same time, then returning to the starting position."

        private const val URI_PUSH_UP = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.push_up_demo
        private const val URI_SIT_UP = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.sit_up_demo
        private const val URI_SQUAT = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.squat_demo
        private const val URI_PULL_UP = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.pull_up_demo
        private const val URI_PLANK = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.plank_demo
        private const val URI_JUMPING_JACK = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.jumping_jack_demo

        val FITNESS_ACTIONS = listOf(
            FitnessActions(ACTION_PUSH_UP, EASY, DESC_PUSH_UP, URI_PUSH_UP),
            FitnessActions(ACTION_SIT_UP, EASY, DESC_SIT_UP, URI_SIT_UP),
            FitnessActions(ACTION_SQUAT, MEDIUM, DESC_SQUAT, URI_SQUAT),
            FitnessActions(ACTION_PLANK, MEDIUM, DESC_PLANK, URI_PLANK),
            FitnessActions(ACTION_JUMPING_JACK, MEDIUM, DESC_JUMPING_JACK, URI_JUMPING_JACK),
            FitnessActions(ACTION_PULL_UP, HARD, DESC_PULL_UP, URI_PULL_UP)
        )

        // ROOM Database
        const val DATABASE_NAME = "fitness_results_database"
        const val FITNESS_RESULTS_TABLE = "fitness_results_table"
    }
}