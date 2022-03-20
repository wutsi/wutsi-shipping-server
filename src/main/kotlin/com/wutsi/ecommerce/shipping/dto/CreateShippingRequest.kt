package com.wutsi.ecommerce.shipping.dto

import kotlin.Long
import kotlin.String

public data class CreateShippingRequest(
    public val type: String = "",
    public val country: String = "",
    public val cityId: Long? = null,
    public val message: String? = null,
    public val street: String? = null,
    public val zipCode: String? = null
)
