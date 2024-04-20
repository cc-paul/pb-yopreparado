package com.ajv.yopreparado.data

data class municipalityDataResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: MunicipalityData
)

data class MunicipalityData(
    val rows_returned: Int,
    val municipality: List<Municipality>
)

data class Municipality(
    val id: Int,
    val barangayName: String,
    val totalPopulation: String,
    val isActive: Int,
    val lat: String,
    val lng: String
)

