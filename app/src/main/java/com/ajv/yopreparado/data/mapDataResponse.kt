package com.ajv.yopreparado.data

data class mapDataResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: Data
)

data class Data(
    val rows_returned: Int,
    val event: List<Event>
)

data class Event(
    val markerID: Int,
    val eventID : Int,
    val event: String,
    val isActive: Int,
    val dateCreated: String,
    val description: String,
    val hasImage: Int,
    val origin: String?,
    val needRadius: Int,
    val link: String,
    val lat : String,
    val lng : String,
    val radius : Double,
    val remarks : String,
    val alertLevel: String,
    val passableVehicle: String,
    val dateReported : String,
    val duration : Long
)