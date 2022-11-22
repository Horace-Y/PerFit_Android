package com.example.perfit.dataSources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FitnessResultEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(FitnessResultTypeConverter::class)
abstract class FitnessResultsDatabase: RoomDatabase() {
    abstract fun fitnessResultDAO(): FitnessResultsDAO
}