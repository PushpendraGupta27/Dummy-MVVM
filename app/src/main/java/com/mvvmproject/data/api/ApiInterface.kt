package com.mvvmproject.data.api

import com.mvvmproject.domain.response.AllWorkOutResponse
import com.mvvmproject.domain.response.CarouselListResponse
import com.mvvmproject.domain.response.IndividualWorkoutApiResponse
import com.mvvmproject.domain.response.NewWorkoutResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {
    @POST("get_carousel_list")
    suspend fun getCarouselList(@Header("Auth-Token") token: String?): Response<CarouselListResponse>

    @POST("get_new_workouts")
    suspend fun getNewWorkout(@Header("Auth-Token") token: String?): Response<NewWorkoutResponse>

    @POST("get_all_workouts")
    suspend fun getAllWorkout(@Header("Auth-Token") token: String?): Response<AllWorkOutResponse>

    @FormUrlEncoded
    @POST("get_workout_details")
    suspend fun getIndividualWorkoutDetails(
        @Header("Auth-Token") token: String?,
        @Field("post_id") postId: Int?,
        @Field("timezone") timeZone: String?,
    ): Response<IndividualWorkoutApiResponse>
}
