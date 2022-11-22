package com.example.perfit.dataSources.database

import androidx.room.TypeConverter
import com.example.perfit.models.FitnessResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FitnessResultTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun fitnessResultToString(fitnessResult: FitnessResult): String{
        return gson.toJson(fitnessResult)
    }

    @TypeConverter
    fun stringToFitnessResult(data: String): FitnessResult{
        val listType = object: TypeToken<FitnessResult>() {}.type
        return gson.fromJson(data, listType)
    }
}