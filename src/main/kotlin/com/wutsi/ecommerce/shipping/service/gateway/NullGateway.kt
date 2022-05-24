package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.ShippingGateway
import com.wutsi.platform.tenant.dto.Tenant

class NullGateway : ShippingGateway {
    override fun enabled(tenant: Tenant) = true

    override fun accept(request: SearchRateRequest, shipping: ShippingEntity): Boolean =
        false
}
