package com.mvvmproject.domain.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchWorkoutResponse(
    @SerializedName("workouts")
    var workouts: List<Workout> = emptyList(),
) : Parcelable

@Parcelize
data class Workout(
    @SerializedName("duration")
    var duration: Int?,
    @SerializedName("favorite_flag")
    var favoriteFlag: Int?,
    @SerializedName("fitness_level")
    var fitnessLevel: String?,
    @SerializedName("post_id")
    var postId: Int?,
    @SerializedName("post_name")
    var postName: String?,
    @SerializedName("thumbnail")
    var thumbnail: String?,
) : Parcelable
