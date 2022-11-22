package com.example.perfit.dataSources

import com.example.perfit.dataSources.database.FitnessResultEntity
import com.example.perfit.dataSources.database.FitnessResultsDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val fitnessResultsDAO: FitnessResultsDAO
){
    suspend fun addNewResult(fitnessResultEntity: FitnessResultEntity) {
        return fitnessResultsDAO.addNewResult(fitnessResultEntity)
    }

    fun readResults(): Flow<List<FitnessResultEntity>> {
        return fitnessResultsDAO.readResults()
    }

    suspend fun deleteResult(favoritesEntity: FitnessResultEntity){
        fitnessResultsDAO.deleteResult(favoritesEntity)
    }
}