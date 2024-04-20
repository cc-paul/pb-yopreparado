package com.ajv.yopreparado.data

data class videoListResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: VideoData2
)

data class VideoData2(
    val rows_returned: Int,
    val videos: List<Video2>
)

data class Video2(
    val id: Int,
    val event: String,
    val videolink: String,
    val imagelink: String,
    val title: String,
    val views: Int
)