package com.zandroid.filimo_mvvm.data.models.register


import com.google.gson.annotations.SerializedName

data class ResponseRegister(
    @SerializedName("ALL_IN_ONE_VIDEO")
    val aLLINONEVIDEO: List<ALLINONEVIDEO?>?
) {
    data class ALLINONEVIDEO(
        @SerializedName("msg")
        val msg: String?,
        @SerializedName("success")
        val success: String?
    )
}