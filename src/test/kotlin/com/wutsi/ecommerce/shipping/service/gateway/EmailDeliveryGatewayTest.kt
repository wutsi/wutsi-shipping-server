package com.wutsi.ecommerce.shipping.service.gateway

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.ecommerce.catalog.WutsiCatalogApi
import com.wutsi.ecommerce.catalog.dto.GetProductResponse
import com.wutsi.ecommerce.catalog.entity.ProductType
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.Address
import com.wutsi.ecommerce.order.dto.ChangeStatusRequest
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.dto.OrderItem
import com.wutsi.ecommerce.order.entity.OrderStatus
import com.wutsi.ecommerce.shipping.dto.Product
import com.wutsi.ecommerce.shipping.dto.SearchRateRequest
import com.wutsi.ecommerce.shipping.entity.ShippingEntity
import com.wutsi.platform.account.WutsiAccountApi
import com.wutsi.platform.account.dto.Account
import com.wutsi.platform.account.dto.GetAccountResponse
import com.wutsi.platform.mail.WutsiMailApi
import com.wutsi.platform.mail.dto.SendMessageRequest
import com.wutsi.platform.mail.dto.SendMessageResponse
import com.wutsi.platform.tenant.dto.Tenant
import com.wutsi.platform.tenant.dto.Toggle
import com.wutsi.platform.tenant.entity.ToggleName
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class EmailDeliveryGatewayTest {
    private val orderApi = mock<WutsiOrderApi>()
    private val mailApi = mock<WutsiMailApi>()
    private val catalogApi = mock<WutsiCatalogApi>()
    private val accountApi = mock<WutsiAccountApi>()
    private val gateway = EmailDeliveryGateway(
        orderApi = orderApi,
        mailApi = mailApi,
        catalogApi = catalogApi,
        accountApi = accountApi,
        logger = mock()
    )

    @Test
    fun enabled() {
        assertFalse(gateway.enabled(createTenant(ToggleName.SHIPPING_IN_STORE_PICKUP)))
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

    @Test
    fun onOrderDone() {
        // GIVEN
        val product = com.wutsi.ecommerce.catalog.dto.Product(
            id = 1L,
            title = "This is the product",
            numericFileUrl = "https://www.g.com/1.pfd",
            type = ProductType.NUMERIC.name
        )
        doReturn(GetProductResponse(product)).whenever(catalogApi).getProduct(1L)

        doReturn(SendMessageResponse("xxx")).whenever(mailApi).sendMessage(any())

        val account = Account(language = "en")
        doReturn(GetAccountResponse(account)).whenever(accountApi).getAccount(any())

        // THEN
        val order = Order(
            id = "1111",
            items = listOf(
                OrderItem(productId = 1L),
            ),
            shippingAddress = Address(
                firstName = "Ray",
                lastName = "Sponsible",
                email = "ray.sponsible@gmail.com"
            )
        )
        gateway.onOrderDone(order)

        // THEN
        verify(orderApi).changeStatus(order.id, ChangeStatusRequest(status = OrderStatus.IN_TRANSIT.name))

        val req = argumentCaptor<SendMessageRequest>()
        verify(mailApi).sendMessage(req.capture())
        assertEquals(order.shippingAddress?.email, req.firstValue.recipient.email)
        assertEquals("Ray Sponsible", req.firstValue.recipient.displayName)

        verify(orderApi).changeStatus(order.id, ChangeStatusRequest(status = OrderStatus.DELIVERED.name))
    }
}
