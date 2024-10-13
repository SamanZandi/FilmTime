package com.zandroid.filimo_mvvm.data.models.register

import com.google.gson.annotations.SerializedName

data class StoredResponseModel (
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("success")
    val success: String?,
    @SerializedName("email")
    var email: String = "",
)