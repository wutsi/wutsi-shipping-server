package com.wutsi.ecommerce.shipping.service.gateway

import com.nhaarman.mockitokotlin2.mock
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.dto.Toggle
import com.wutsi.platform.tenant.entity.ToggleName
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class InStorePickupGatewayTest {
    private val gateway = InStorePickupGateway(mock())

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