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

internal class InternationalDeliveryGatewayTest {
    private val gateway = InternationalDeliveryGateway(mock())

    @Test
    fun enabled() {
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_IN_STORE_PICKUP)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_EMAIL_DELIVERY)))
        assertTrue(gateway.enabled(createTenant(ToggleName.SHIPPING_INTERNATIONAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_PICKUP)))
    }

    private fun createTenant(toggle: ToggleName) = Tenant(
        toggles = listOf(Toggle(name = toggle.name))
    )

    @Test
    fun sameCountry() {
        val request = SearchRateRequest(
            country = "CM",
            products = listOf(
                Product(productId = 111, productType = ProductType.PHYSICAL.name)
            )
        )
        val shipping = ShippingEntity(country = "cm")
        assertTrue(gateway.accept(request, shipping))
    }

    @Test
    fun differentCountry() {
        val request = SearchRateRequest(country = "CM")
        val shipping = ShippingEntity(country = null)
        assertFalse(gateway.accept(request, shipping))
    }

    @Test
    fun nullCountry() {
        val request = SearchRateRequest(country = "CM")
        val shipping = ShippingEntity(country = "FR")
        assertFalse(gateway.accept(request, shipping))
    }
}
