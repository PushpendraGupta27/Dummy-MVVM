package com.mvvmproject.data.repository

import com.mvvmproject.data.api.ApiInterface
import com.mvvmproject.domain.response.AllWorkOutResponse
import com.mvvmproject.domain.response.CarouselListResponse
import com.mvvmproject.domain.response.NewWorkoutResponse
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun carouselApi(token: String): Response<CarouselListResponse> =
        apiInterface.getCarouselList(token)

    suspend fun newWorkoutApi(token: String): Response<NewWorkoutResponse> =
        apiInterface.getNewWorkout(token)

    suspend fun allWorkoutApi(token: String): Response<AllWorkOutResponse> =
        apiInterface.getAllWorkout(token)
}