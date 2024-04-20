package com.ajv.yopreparado.data

data class notificationResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: NotificationData
)

data class NotificationData(
    val rows_returned: Int,
    val notification: List<NotificationItems>
)

data class NotificationItems (
    val eventID: Int,
    val title: String,
    val body: String,
    val dateCreated: String,
    val event: String,
    val imageLink: String
)