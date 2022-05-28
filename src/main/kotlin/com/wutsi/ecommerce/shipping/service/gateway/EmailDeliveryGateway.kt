package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.Gateway
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.entity.ToggleName
import org.springframework.stereotype.Service

@Service
class EmailDeliveryGateway(
    private val logger: KVLogger
) : Gateway {
    override fun enabled(tenant: Tenant): Boolean =
        tenant.toggles.find { it.name == ToggleName.SHIPPING_EMAIL_DELIVERY.name } != null

    /**
     * Accept if any product is numeric
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean {
        val result = request.products.find { it.productType == ProductType.NUMERIC.name } != null

        logger.add("gateway_email", result)
        return result
    }
}
