package com.ajv.yopreparado.data

import com.google.gson.annotations.SerializedName

data class passwordDataResponse (
    val statusCode: Int,
    val success: Boolean,
    val messages: ArrayList<String>,
    val data: ArrayList<String>
)
