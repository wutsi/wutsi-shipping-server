package com.wutsi.ecommerce.shipping.dto

import kotlin.String

public data class ChangeStatusRequest(
    public val status: String = "",
    public val reason: String? = null,
    public val comment: String? = null
)
