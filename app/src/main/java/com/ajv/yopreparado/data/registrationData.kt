package com.ajv.yopreparado.data

import com.google.gson.annotations.SerializedName

data class registrationData (
    @SerializedName("firstname") val firstName : String,
    @SerializedName("middlename") val middlename : String,
    @SerializedName("lastname") val lastname : String,
    @SerializedName("username") val username : String,
    @SerializedName("password") val password : String,
    @SerializedName("rpassword") val rpassword : String,
    @SerializedName("emailaddress") val emailaddress : String,
    @SerializedName("fcm") val fcm : String
)

//