package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.entity.ToggleName
import org.springframework.stereotype.Service

@Service
class InternationalDeliveryGateway(
    private val logger: KVLogger
) : ShippingGateway {
    override fun enabled(tenant: Tenant): Boolean =
        tenant.toggles.find { it.name == ToggleName.SHIPPING_INTERNATIONAL_DELIVERY.name } != null

    /**
     * Accept if the shopping contry is different
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean {
        val result = shipping.country != null && shipping.country.equals(request.country, true)

        logger.add("gateway_international", result)
        logger.add("gateway_international_country", shipping.country)
        return result
    }
}
