package com.mvvmproject.data.repository

import com.mvvmproject.data.api.ApiInterface
import com.mvvmproject.domain.response.IndividualWorkoutApiResponse
import retrofit2.Response
import javax.inject.Inject

class VideoDetailsRepository @Inject constructor(private val apiInterface: ApiInterface) {
    suspend fun videoDetailsApi(
        token: String,
        postId: Int,
        timeZone: String,
    ): Response<IndividualWorkoutApiResponse> =
        apiInterface.getIndividualWorkoutDetails(token, postId, timeZone)
}