package com.wutsi.ecommerce.shipping.service

import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.service.gateway.EmailDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.InStorePickupGateway
import com.wutsi.ecommerce.shipping.service.gateway.InternationalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalPickupGateway
import org.springframework.stereotype.Service

@Service
class GatewayProvider(
    private val store: InStorePickupGateway,
    private val email: EmailDeliveryGateway,
    private val pickup: LocalPickupGateway,
    private val international: InternationalDeliveryGateway,
    private val local: LocalDeliveryGateway
) {
    fun get(shipping: ShippingEntity): ShippingGateway =
        when (shipping.type) {
            ShippingType.IN_STORE_PICKUP -> store
            ShippingType.EMAIL_DELIVERY -> email
            ShippingType.LOCAL_PICKUP -> pickup
            ShippingType.INTERNATIONAL_SHIPPING -> international
            ShippingType.LOCAL_DELIVERY -> local
        }
}
