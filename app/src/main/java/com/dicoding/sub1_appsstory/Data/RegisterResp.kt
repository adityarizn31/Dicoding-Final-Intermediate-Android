package com.dicoding.sub1_appsstory.Data

import com.google.gson.annotations.SerializedName

data class RegisterResp(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)
