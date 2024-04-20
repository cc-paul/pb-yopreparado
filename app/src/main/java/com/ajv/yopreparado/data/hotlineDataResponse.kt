package com.ajv.yopreparado.data

data class hotlineDataResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: HotlineData
)

data class HotlineData(
    val rows_returned: Int,
    val hotline: List<Hotline>
)

data class Hotline(
    val id: Int,
    val hotline: String,
    val mobileNumber: String,
    val telephoneNumber: String,
    val emailAddress: String,
    val isActive: Int,
    val dateCreated: String,
    val isRemoved: Int
)