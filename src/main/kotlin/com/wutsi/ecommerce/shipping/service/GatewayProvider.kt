package com.wutsi.ecommerce.shipping.service

import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.service.gateway.EmailDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.InternationalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalPickupGateway
import org.springframework.stereotype.Service

@Service
class GatewayProvider {
    fun get(shipping: ShippingEntity): ShippingGateway =
        when (shipping.type) {
            ShippingType.EMAIL_DELIVERY -> EmailDeliveryGateway()
            ShippingType.LOCAL_PICKUP -> LocalPickupGateway()
            ShippingType.INTERNATIONAL_SHIPPING -> InternationalDeliveryGateway()
            ShippingType.LOCAL_DELIVERY -> LocalDeliveryGateway()
        }
}
