package com.wutsi.ecommerce.shipping.service.gateway

import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

internal class NullGatewayTest {
    private val gateway = NullGateway()

    @Test
    fun sameCity() {
        val request = SearchRateRequest()
        val shipping = ShippingEntity()

        assertFalse(gateway.accept(request, shipping))
    }
}
