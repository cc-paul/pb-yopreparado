package com.ajv.yopreparado.data

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class registrationResponse (
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: List<String>
)