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

internal class EmailDeliveryGatewayTest {
    private val gateway = EmailDeliveryGateway(mock())

    @Test
    fun enabled() {
        assertTrue(gateway.enabled(createTenant(ToggleName.SHIPPING_EMAIL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_INTERNATIONAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_DELIVERY)))
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_LOCAL_PICKUP)))
    }

    private fun createTenant(toggle: ToggleName) = Tenant(
        toggles = listOf(Toggle(name = toggle.name))
    )

    @Test
    fun allProductNumeric() {
        val request = SearchRateRequest(
            products = listOf(
                Product(productId = 111, productType = ProductType.NUMERIC.name),
                Product(productId = 112, productType = ProductType.NUMERIC.name)
            )
        )

        assertTrue(gateway.accept(request, ShippingEntity()))
    }

    @Test
    fun someProductNumeric() {
        val request = SearchRateRequest(
            products = listOf(
                Product(productId = 111, productType = ProductType.NUMERIC.name),
                Product(productId = 112, productType = ProductType.PHYSICAL.name)
            )
        )

        assertTrue(gateway.accept(request, ShippingEntity()))
    }

    @Test
    fun noProductNumeric() {
        val request = SearchRateRequest(
            products = listOf(
                Product(productId = 111, productType = ProductType.PHYSICAL.name),
                Product(productId = 112, productType = ProductType.PHYSICAL.name)
            )
        )

        assertFalse(gateway.accept(request, ShippingEntity()))
    }
}
