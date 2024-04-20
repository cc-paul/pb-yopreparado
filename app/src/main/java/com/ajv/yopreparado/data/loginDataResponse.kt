package com.ajv.yopreparado.data

data class loginDataResponse(
    val session_id: Int,
    val first_name: String,
    val middle_name: String,
    val last_name: String,
    val email_address: String,
    val access_token: String,
    val access_token_expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
    val user_id: Int,
    val image_link: String
)
