package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service

@Service
class InternationalDeliveryGateway(
    private val logger: KVLogger
) : ShippingGateway {
    /**
     * Accept if the shopping contry is different
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean {
        val result = shipping.country != null && shipping.country.equals(request.country, true)

        logger.add("gateway_international", result)
        return result
    }
}
