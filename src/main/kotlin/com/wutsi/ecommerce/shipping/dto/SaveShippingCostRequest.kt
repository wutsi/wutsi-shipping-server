package com.wutsi.ecommerce.shipping.dto

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class SaveShippingCostRequest(
    public val country: String = "",
    public val cityId: Long? = null,
    public val primaryCost: Double = 0.0,
    public val additionalCost: Double = 0.0
)
