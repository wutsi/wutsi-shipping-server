package com.wutsi.ecommerce.shipping.endpoint

import com.wutsi.ecommerce.shipping.dto.GetShippingOrderResponse
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import com.wutsi.ecommerce.shipping.entity.ShippingType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetShippingOrderController.sql"])
class GetShippingOrderControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun invoke() {
        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders/100"
        val response = rest.getForEntity(url, GetShippingOrderResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val shippingOrder = response.body!!.shippingOrder
        assertEquals(555L, shippingOrder.accountId)
        assertEquals(666L, shippingOrder.merchantId)
        assertEquals("777", shippingOrder.orderId)
        assertEquals(ShippingOrderStatus.IN_TRANSIT.name, shippingOrder.status)

        val shipping = shippingOrder.shipping
        assertEquals(100, shipping.id)
        assertEquals(true, shipping.enabled)
        assertEquals(ShippingType.LOCAL_PICKUP.name, shipping.type)
        assertEquals("Local Pickup", shipping.message)
        assertEquals("CM", shipping.country)
        assertEquals(1111L, shipping.cityId)
        assertEquals(30000.0, shipping.rate)
        assertEquals("XAF", shipping.currency)
    }

    @Test
    fun notFound() {
        val url = "http://localhost:$port/v1/shipping-orders/999"
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url, GetShippingOrderResponse::class.java)
        }

        assertEquals(404, ex.rawStatusCode)
    }
}
