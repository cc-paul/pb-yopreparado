package com.ajv.yopreparado.data

import com.google.gson.annotations.SerializedName

data class loginData(
    @SerializedName("username") val username : String,
    @SerializedName("password") val password : String,
    @SerializedName("fcmtoken") val fcmtoken : String
)
