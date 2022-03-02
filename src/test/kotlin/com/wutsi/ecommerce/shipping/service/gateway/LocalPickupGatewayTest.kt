package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class LocalPickupGatewayTest {
    private val gateway = LocalPickupGateway()

    @Test
    fun sameCity() {
        val request = SearchRateRequest(cityId = 11)
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
