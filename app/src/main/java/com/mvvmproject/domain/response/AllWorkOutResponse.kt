package com.mvvmproject.domain.response
import com.google.gson.annotations.SerializedName

data class AllWorkOutResponse(
    @SerializedName("workout_list")
    var workoutList: List<WorkoutList> = emptyList()
)

data class WorkoutList(
    @SerializedName("display_name")
    var displayName: String?,
    @SerializedName("workout_type")
    var workoutType: String?,
    @SerializedName("workouts")
    var workouts: List<Workout> = emptyList()
)