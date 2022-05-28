package com.wutsi.ecommerce.shipping.service.gateway

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.ChangeStatusRequest
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.entity.OrderStatus
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.ecommerce.shipping.service.Gateway
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.dto.Toggle
import com.wutsi.platform.tenant.entity.ToggleName
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InStorePickupGatewayTest {
    private lateinit var orderApi: WutsiOrderApi
    private lateinit var logger: KVLogger
    private lateinit var gateway: Gateway

    @BeforeEach
    fun setUp() {
        orderApi = mock()
        logger = mock()
        gateway = InStorePickupGateway(orderApi, logger)
    }

    @Test
    fun onOrderDone() {
        val order = Order(id = "555")
        gateway.onOrderDone(order)

        verify(orderApi).changeStatus(
            id = order.id,
            request = ChangeStatusRequest(status = OrderStatus.READY_FOR_PICKUP.name)
        )
    }

    @Test
    fun enabled() {
        assertTrue(gateway.enabled(createTenant(ToggleName.SHIPPING_IN_STORE_PICKUP)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_EMAIL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_INTERNATIONAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_PICKUP)))
    }

    private fun createTenant(toggle: ToggleName) = Tenant(
        toggles = listOf(Toggle(name = toggle.name))
    )

    @Test
    fun accept() {
        val request = SearchRateRequest()
        val shipping = ShippingEntity()

        assertTrue(gateway.accept(request, shipping))
    }
}
