package com.example.perfit.dataSources.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.perfit.models.FitnessResult
import com.example.perfit.utils.Constants.Companion.FITNESS_RESULTS_TABLE

@Entity(tableName = FITNESS_RESULTS_TABLE)
class FitnessResultEntity(
    var fitnessResult: FitnessResult
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}