package com.wutsi.ecommerce.shipping.service

import com.nhaarman.mockitokotlin2.mock
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.entity.ShippingType
import com.wutsi.ecommerce.shipping.service.gateway.EmailDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.InStorePickupGateway
import com.wutsi.ecommerce.shipping.service.gateway.InternationalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalDeliveryGateway
import com.wutsi.ecommerce.shipping.service.gateway.LocalPickupGateway
import com.wutsi.platform.core.logging.KVLogger
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class GatewayProviderTest {
    private val orderApi: WutsiOrderApi = mock()
    private val logger = mock<KVLogger>()
    private val provider = GatewayProvider(
        InStorePickupGateway(orderApi, logger),
        EmailDeliveryGateway(mock(), mock(), mock(), mock(), mock()),
        LocalPickupGateway(logger),
        InternationalDeliveryGateway(logger),
        LocalDeliveryGateway(logger)
    )

    @Test
    fun test() {
        assertTrue(provider.get(createShipping(ShippingType.IN_STORE_PICKUP)) is InStorePickupGateway)
        assertTrue(provider.get(createShipping(ShippingType.LOCAL_PICKUP)) is LocalPickupGateway)
        assertTrue(provider.get(createShipping(ShippingType.LOCAL_DELIVERY)) is LocalDeliveryGateway)
        assertTrue(provider.get(createShipping(ShippingType.INTERNATIONAL_SHIPPING)) is InternationalDeliveryGateway)
        assertTrue(provider.get(createShipping(ShippingType.EMAIL_DELIVERY)) is EmailDeliveryGateway)
    }

    private fun createShipping(type: ShippingType) = ShippingEntity(
        type = type
    )
}
