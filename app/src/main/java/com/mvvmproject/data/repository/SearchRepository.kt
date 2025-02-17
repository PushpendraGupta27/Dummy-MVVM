package com.mvvmproject.data.repository

import com.mvvmproject.data.api.ApiInterface
import com.mvvmproject.domain.response.WorkoutSearchAttributesRes
import retrofit2.Response
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun searchAttributeApi(token: String):Response<WorkoutSearchAttributesRes> = apiInterface.workoutSearchAttributes(token)
}