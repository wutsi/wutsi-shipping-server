package com.wutsi.ecommerce.shipping.service.gateway

import com.nhaarman.mockitokotlin2.mock
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class InternationalDeliveryGatewayTest {
    private val gateway = InternationalDeliveryGateway(mock())

    @Test
    fun sameCountry() {
        val request = SearchRateRequest(country = "CM")
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