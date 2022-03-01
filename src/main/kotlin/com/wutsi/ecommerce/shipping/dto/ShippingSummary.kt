package com.wutsi.ecommerce.shipping.dto

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String

public data class ShippingSummary(
    public val id: Long = 0,
    public val accountId: Long = 0,
    public val type: String = "",
    public val enabled: Boolean = false,
    public val rate: Double? = null,
    public val deliveryTime: Int? = null
)
