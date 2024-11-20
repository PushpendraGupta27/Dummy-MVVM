package com.mvvmproject.domain.response
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class IndividualWorkoutApiResponse(
    @SerializedName("calorie_range")
    var calorieRange: String?,
    @SerializedName("comments")
    var comments: List<Comment> = emptyList(),
    @SerializedName("duration")
    var duration: Int?,
    @SerializedName("favorite_flag")
    var favoriteFlag: Int?,
    @SerializedName("fitness_level")
    var fitnessLevel: String?,
    @SerializedName("muscle_group")
    var muscleGroup: String?,
    @SerializedName("post_desc")
    var postDesc: String?,
    @SerializedName("post_id")
    var postId: Int?,
    @SerializedName("post_name")
    var postName: String?,
    @SerializedName("self_hosted_video")
    var selfHostedVideo: String?,
    @SerializedName("thumbnail")
    var thumbnail: String?,
    @SerializedName("total_comments")
    var totalComments: Int?,
    @SerializedName("workout_type")
    var workoutType: String?,
    @SerializedName("youtube_video_id")
    var youtubeVideoId: String?
)

@Parcelize
data class Comment(
    @SerializedName("comment_id")
    var commentId: Int?,
    @SerializedName("comment_text")
    var commentText: String?,
    @SerializedName("post_datetime")
    var postDatetime: String?,
    @SerializedName("profile_pic")
    var profilePic: String?,
    @SerializedName("user_name")
    var userName: String?,
    @SerializedName("recent_replies")
    var recentReplies: RecentReplies?,
    @SerializedName("total_reply")
    var totalReply: Int?,
):Parcelable

@Parcelize
data class RecentReplies(
    @SerializedName("comment_id")
    var commentId: Int?,
    @SerializedName("comment_text")
    var commentText: String?,
    @SerializedName("post_datetime")
    var postDatetime: String?,
    @SerializedName("profile_pic")
    var profilePic: String?,
    @SerializedName("user_name")
    var userName: String?
):Parcelable