package com.example.perfit.dataSources.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessResultsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addResult(fitnessResultEntity: FitnessResultEntity)

    @Query("SELECT * FROM fitness_results_table ORDER BY id DESC")
    fun readResults(): Flow<List<FitnessResultEntity>>

    @Delete
    suspend fun deleteResult(fitnessResultEntity: FitnessResultEntity)
}