package com.wutsi.ecommerce.shipping.service

import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.service.gateway.EmailDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.InternationalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalPickupGateway
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GatewayProviderTest {
    private val provider = GatewayProvider()

    @Test
    fun test() {
        assertTrue(provider.get(createShipping(ShippingType.LOCAL_PICKUP)) is LocalPickupGateway)
        assertTrue(provider.get(createShipping(ShippingType.LOCAL_DELIVERY)) is LocalDeliveryGateway)
        assertTrue(provider.get(createShipping(ShippingType.INTERNATIONAL_SHIPPING)) is InternationalDeliveryGateway)
        assertTrue(provider.get(createShipping(ShippingType.EMAIL_DELIVERY)) is EmailDeliveryGateway)
    }

    private fun createShipping(type: ShippingType) = ShippingEntity(
        type = type
    )
}
