package com.ajv.yopreparado.recycleview

data class eventItem (
    val eventId : Int = 0,
    val link : String = "",
    val eventName : String = "",
    var isSelected: Boolean = false
)