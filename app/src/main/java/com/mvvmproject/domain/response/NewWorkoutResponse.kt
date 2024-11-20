package com.mvvmproject.domain.response

import com.google.gson.annotations.SerializedName

data class NewWorkoutResponse(
    @SerializedName("new_workouts")
    var newWorkouts: List<NewWorkout> = emptyList(),
)

data class NewWorkout(
    @SerializedName("display_name")
    var displayName: String?,
    @SerializedName("workouts")
    var workouts: List<Workout> = emptyList()
)

