package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway

class LocalPickupGateway : ShippingGateway {
    /**
     * Accept only request in the same city
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean =
        shipping.cityId != null && request.cityId == shipping.cityId
}
