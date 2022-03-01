package com.wutsi.ecommerce.shipping.dto

import kotlin.collections.List

public data class ListShippingResponse(
    public val shippings: List<ShippingSummary> = emptyList()
)
