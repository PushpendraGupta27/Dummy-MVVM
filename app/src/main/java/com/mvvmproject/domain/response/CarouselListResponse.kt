package com.mvvmproject.domain.response
import com.google.gson.annotations.SerializedName

data class CarouselListResponse(
    @SerializedName("carousel_list")
    var carouselList: List<Carousel> = emptyList()
)

data class Carousel(
    @SerializedName("carousel_key")
    var carouselKey: String?,
    @SerializedName("carousel_value")
    var carouselValue: String?,
    @SerializedName("thumbnail")
    var thumbnail: String?
)