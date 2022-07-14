package com.example.perfit.dataSources

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
//    localDataSource: LocalDataSource
){
    val remote = remoteDataSource
//    val local = localDataSource
}