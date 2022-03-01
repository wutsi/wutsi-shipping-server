package com.wutsi.ecommerce.shipping.dto

import kotlin.String

public data class CreateShippingRequest(
    public val type: String = "",
    public val message: String? = null
)
