package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway

class EmailDeliveryGateway : ShippingGateway {
    /**
     * Accept if any product is numeric
     */
    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean =
        request.products.find { it.productType == ProductType.NUMERIC.name } != null
}
