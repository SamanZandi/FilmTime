package com.zandroid.filimo_mvvm.data.models.category


import com.google.gson.annotations.SerializedName

data class ResponseCategory(
    @SerializedName("ALL_IN_ONE_VIDEO")
    val aLLINONEVIDEO: List<ALLINONEVIDEO>?
) {
    data class ALLINONEVIDEO(
        @SerializedName("category_image")
        val categoryImage: String?,
        @SerializedName("category_image_thumb")
        val categoryImageThumb: String?,
        @SerializedName("category_name")
        val categoryName: String,
        @SerializedName("cid")
        val cid: String?
    )
}