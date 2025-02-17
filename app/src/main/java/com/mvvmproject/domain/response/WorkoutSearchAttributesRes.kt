package com.mvvmproject.domain.response
import com.google.gson.annotations.SerializedName


data class WorkoutSearchAttributesRes(
    @SerializedName("equipment_type")
    var equipmentType: EquipmentType?,
    @SerializedName("fitness_level")
    var fitnessLevel: FitnessLevel?,
    @SerializedName("muscle_group")
    var muscleGroup: MuscleGroup?,
    @SerializedName("workout_length")
    var workoutLength: WorkoutLength?,
    @SerializedName("workout_type")
    var workoutType: WorkoutType?
)

data class EquipmentType(
    @SerializedName("list")
    var list: ArrayList<Items>? = null,
)

data class FitnessLevel(
    @SerializedName("list")
    var list: ArrayList<Items>? = null,
)

data class MuscleGroup(
    @SerializedName("list")
    var list: ArrayList<Items>? = null,
)

data class WorkoutLength(
    @SerializedName("max_duration")
    var maxDuration: Int?,
    @SerializedName("min_duration")
    var minDuration: Int?
)

data class WorkoutType(
    @SerializedName("list")
    var list: ArrayList<Items>? = null,
)
data class Items(
    @SerializedName("display_name")
    var displayName: String?,
    @SerializedName("display_order")
    var displayOrder: Int?,
    @SerializedName("attribute_name")
    var attributeName: String?,
    var isSelected: Boolean,
)
