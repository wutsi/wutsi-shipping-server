package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway

class InternationalDeliveryGateway : ShippingGateway {
    /**
     * Not ready yet
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean =
        shipping.country != null && shipping.country.equals(request.country, true)
}
