package com.ajv.yopreparado.data

import com.google.gson.annotations.SerializedName

data class loginResponse (
    val statusCode: Int,
    val success: Boolean,
    val messages: ArrayList<String>,
    val data: loginDataResponse
)
