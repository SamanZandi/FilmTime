package com.zandroid.filimo_mvvm.data.models.detail


import com.google.gson.annotations.SerializedName

data class ResponseSingleMovie(
    @SerializedName("ALL_IN_ONE_VIDEO")
    val aLLINONEVIDEO: List<ALLINONEVIDEO>,

) {
    data class ALLINONEVIDEO(
        @SerializedName("cat_id")
        val catId: String?,
        @SerializedName("category_name")
        val categoryName: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("rate_avg")
        val rateAvg: String?,
        @SerializedName("related")
        val related: List<Related?>?,
        @SerializedName("totel_viewer")
        val totelViewer: String?,
        @SerializedName("user_comments")
        val userComments: List<Any?>?,
        @SerializedName("video_description")
        val videoDescription: String?,
        @SerializedName("video_duration")
        val videoDuration: String?,
        @SerializedName("video_id")
        val videoId: String?,
        @SerializedName("video_thumbnail_b")
        val videoThumbnailB: String?,
        @SerializedName("video_thumbnail_s")
        val videoThumbnailS: String?,
        @SerializedName("video_title")
        val videoTitle: String?,
        @SerializedName("video_type")
        val videoType: String?,
        @SerializedName("video_url")
        val videoUrl: String?
    ) {

        data class Related(
            val related: List<Related>,
            @SerializedName("cat_id")
            val catId: String?,
            @SerializedName("category_name")
            val categoryName: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("rate_avg")
            val rateAvg: String?,
            @SerializedName("totel_viewer")
            val totelViewer: String?,
            @SerializedName("video_description")
            val videoDescription: String?,
            @SerializedName("video_duration")
            val videoDuration: String?,
            @SerializedName("video_id")
            val videoId: String?,
            @SerializedName("video_thumbnail_b")
            val videoThumbnailB: String?,
            @SerializedName("video_thumbnail_s")
            val videoThumbnailS: String?,
            @SerializedName("video_title")
            val videoTitle: String?,
            @SerializedName("video_type")
            val videoType: String?,
            @SerializedName("video_url")
            val videoUrl: String?
        )
    }
}