package com.wutsi.ecommerce.shipping.dto

import kotlin.Boolean
import kotlin.String

public data class UpdateShippingRequest(
    public val message: String? = null,
    public val enabled: Boolean = false
)
