package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service

@Service
class LocalPickupGateway(
    private val logger: KVLogger
) : ShippingGateway {
    /**
     * Accept only request in the same city
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean {
        val result = shipping.cityId != null && request.cityId == shipping.cityId

        logger.add("gateway_pickup", result)
        return result
    }
}
