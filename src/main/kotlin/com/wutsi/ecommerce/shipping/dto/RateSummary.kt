package com.wutsi.ecommerce.shipping.dto

import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class RateSummary(
    public val shippingId: Long = 0,
    public val shippingType: String = "",
    public val rate: Double = 0.0,
    public val currency: String = "",
    public val deliveryTime: Int? = null
)
