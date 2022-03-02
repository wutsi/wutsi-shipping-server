package com.wutsi.ecommerce.shipping.service

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity

interface ShippingGateway {
    fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean

    fun computeRate(shipping: ShippingEntity): Double = shipping.rate ?: 0.0
}
