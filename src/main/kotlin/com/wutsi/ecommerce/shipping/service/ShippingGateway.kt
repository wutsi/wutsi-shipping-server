package com.wutsi.ecommerce.shipping.service

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.platform.tenant.dto.Tenant

interface ShippingGateway {
    fun enabled(tenant: Tenant): Boolean

    fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean

    fun computeRate(shipping: ShippingEntity): Double = shipping.rate ?: 0.0
}
