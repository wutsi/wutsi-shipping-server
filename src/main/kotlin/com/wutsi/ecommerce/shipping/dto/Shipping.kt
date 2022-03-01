package com.wutsi.ecommerce.shipping.dto

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public data class Shipping(
    public val id: Long = 0,
    public val accountId: Long = 0,
    public val type: String = "",
    public val message: String? = null,
    public val enabled: Boolean = false,
    public val rates: List<Rate> = emptyList()
)
