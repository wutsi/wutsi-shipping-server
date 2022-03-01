package com.wutsi.ecommerce.shipping.dto

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class SaveRateRequest(
    public val shippingId: Long = 0,
    public val country: String = "",
    public val cityId: Long? = null,
    public val amount: Double = 0.0
)
