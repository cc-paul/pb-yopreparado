package com.ajv.yopreparado.data

data class profileDataResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: List<ImageLinkData>
)

data class ImageLinkData(
    val image_link: String
)