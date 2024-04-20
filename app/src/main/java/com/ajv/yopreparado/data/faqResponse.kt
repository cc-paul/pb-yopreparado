package com.ajv.yopreparado.data

data class faqResponse(
    val statusCode: Int,
    val success: Boolean,
    val messages: List<String>,
    val data: FAQData
)

data class FAQData(
    val rows_returned: Int,
    val faq: List<FAQ>
)

data class FAQ(
    val id: Int,
    val question: String,
    val answer: String
)
