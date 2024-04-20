package com.ajv.yopreparado.data

class videoResponse (
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: VideoData
)

data class VideoData(
    val rows_returned: Int,
    val video: List<Video>
)

data class Video(
    val id: Int,
    val event: String,
    val description: String,
    val origin: String?,
    val videolink: String,
    val imagelink: String,
    val `do`: List<DoItem>
)

data class DoItem(
    val id: Int,
    val isDo: Int,
    val details: String,
    val category: String
)