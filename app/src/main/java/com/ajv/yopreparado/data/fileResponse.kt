package com.ajv.yopreparado.data

data class FileResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: FileData
)

data class FileData(
    val rows_returned: Int,
    val files: List<FileDetail>
)

data class FileDetail(
    val id: Int,
    val filename: String,
    val type: String,
    val link: String
)
