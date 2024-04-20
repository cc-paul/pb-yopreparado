package com.ajv.yopreparado.data

data class passwordResetResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: ArrayList<String>,
    val data: ArrayList<String>
)
