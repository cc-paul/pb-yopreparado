package com.ajv.yopreparado.data

data class viewResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: Any?
)