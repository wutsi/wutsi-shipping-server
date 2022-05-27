package com.wutsi.ecommerce.shipping.dto

import kotlin.Long
import kotlin.String

public data class ShippingOrder(
    public val id: Long = 0,
    public val orderId: String = "",
    public val merchantId: Long = 0,
    public val customerId: Long = 0,
    public val status: String = "",
    public val shipping: Shipping = Shipping()
)
