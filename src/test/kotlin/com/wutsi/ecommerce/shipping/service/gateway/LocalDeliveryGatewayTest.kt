package com.wutsi.ecommerce.shipping.service.gateway

import com.nhaarman.mockitokotlin2.mock
import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.shipping.dto.Product
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.dto.Toggle
import com.wutsi.platform.tenant.entity.ToggleName
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class LocalDeliveryGatewayTest {
    private val gateway = LocalDeliveryGateway(mock())

    @Test
    fun enabled() {
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_IN_STORE_PICKUP)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_EMAIL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_INTERNATIONAL_DELIVERY)))
        assertTrue(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_PICKUP)))
    }

    private fun createTenant(toggle: ToggleName) = Tenant(
        toggles = listOf(Toggle(name = toggle.name))
    )

    @Test
    fun sameCity() {
        val request = SearchRateRequest(
            cityId = 11,
            products = listOf(
                Product(productId = 111, productType = ProductType.PHYSICAL.name)
            )
        )
        val shipping = ShippingEntity(cityId = 11L)

        assertTrue(gateway.accept(request, shipping))
    }

    @Test
    fun differentCity() {
        val request = SearchRateRequest(cityId = 1221)
        val shipping = ShippingEntity(cityId = 11L)

        assertFalse(gateway.accept(request, shipping))
    }

    @Test
    fun nullCity() {
        val request = SearchRateRequest(cityId = 111L)
        val shipping = ShippingEntity(cityId = null)

        assertFalse(gateway.accept(request, shipping))
    }
}
