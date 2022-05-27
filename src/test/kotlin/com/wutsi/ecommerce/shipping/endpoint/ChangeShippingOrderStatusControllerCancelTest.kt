package com.wutsi.ecommerce.shipping.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.dao.ShippingOrderStatusRepository
import com.wutsi.ecommerce.shipping.dto.ChangeStatusRequest
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import com.wutsi.ecommerce.shipping.error.ErrorURN
import com.wutsi.ecommerce.shipping.event.EventURN
import com.wutsi.ecommerce.shipping.event.ShippingOrderEventPayload
import com.wutsi.platform.core.error.ErrorResponse
import com.wutsi.platform.core.stream.EventStream
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/ChangeShippingOrderStatusController.sql"])
class ChangeShippingOrderStatusControllerCancelTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    @MockBean
    private lateinit var eventStream: EventStream

    @Autowired
    private lateinit var shippingOrderDao: ShippingOrderRepository

    @Autowired
    private lateinit var statusDao: ShippingOrderStatusRepository

    @Test
    fun created() {
        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders/100/status"
        val response = rest.postForEntity(
            url,
            ChangeStatusRequest(
                status = ShippingOrderStatus.CANCELLED.name,
                "no_inventory",
                "None of the product is available"
            ),
            Any::class.java
        )

        // THEN
        assertEquals(200, response.statusCodeValue)

        val order = shippingOrderDao.findById(100).get()
        assertEquals(ShippingOrderStatus.CANCELLED, order.status)

        verify(eventStream).publish(
            EventURN.SHIPPING_CANCELLED.urn,
            ShippingOrderEventPayload(100L)
        )

        val statuses = statusDao.findByShippingOrder(order)
        assertEquals(1, statuses.size)
        assertEquals(100, statuses[0].shippingOrder.id)
        assertEquals(ShippingOrderStatus.CANCELLED, statuses[0].status)
        assertEquals(ShippingOrderStatus.CREATED, statuses[0].previousStatus)
        assertNotNull(statuses[0].created)
        assertEquals("no_inventory", statuses[0].reason)
        assertEquals("None of the product is available", statuses[0].comment)
    }

    @Test
    fun inTransit() {
        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders/101/status"
        val response = rest.postForEntity(
            url,
            ChangeStatusRequest(
                status = ShippingOrderStatus.CANCELLED.name,
                "no_inventory",
                "None of the product is available"
            ),
            Any::class.java
        )

        // THEN
        assertEquals(200, response.statusCodeValue)

        val order = shippingOrderDao.findById(101).get()
        assertEquals(ShippingOrderStatus.CANCELLED, order.status)

        verify(eventStream).publish(
            EventURN.SHIPPING_CANCELLED.urn,
            ShippingOrderEventPayload(101L)
        )

        val statuses = statusDao.findByShippingOrder(order)
        assertEquals(1, statuses.size)
        assertEquals(101, statuses[0].shippingOrder.id)
        assertEquals(ShippingOrderStatus.CANCELLED, statuses[0].status)
        assertEquals(ShippingOrderStatus.IN_TRANSIT, statuses[0].previousStatus)
        assertNotNull(statuses[0].created)
        assertEquals("no_inventory", statuses[0].reason)
        assertEquals("None of the product is available", statuses[0].comment)
    }

    @Test
    fun readyForPickup() {
        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders/102/status"
        val response = rest.postForEntity(
            url,
            ChangeStatusRequest(
                status = ShippingOrderStatus.CANCELLED.name,
                "no_inventory",
                "None of the product is available"
            ),
            Any::class.java
        )

        // THEN
        assertEquals(200, response.statusCodeValue)

        val order = shippingOrderDao.findById(102).get()
        assertEquals(ShippingOrderStatus.CANCELLED, order.status)

        verify(eventStream).publish(
            EventURN.SHIPPING_CANCELLED.urn,
            ShippingOrderEventPayload(102L)
        )

        val statuses = statusDao.findByShippingOrder(order)
        assertEquals(1, statuses.size)
        assertEquals(102, statuses[0].shippingOrder.id)
        assertEquals(ShippingOrderStatus.CANCELLED, statuses[0].status)
        assertEquals(ShippingOrderStatus.READY_FOR_PICKUP, statuses[0].previousStatus)
        assertNotNull(statuses[0].created)
        assertEquals("no_inventory", statuses[0].reason)
        assertEquals("None of the product is available", statuses[0].comment)
    }

    @Test
    fun delivered() {
        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders/103/status"
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(
                url,
                ChangeStatusRequest(
                    status = ShippingOrderStatus.CANCELLED.name,
                ),
                Any::class.java
            )
        }

        // THEN
        assertEquals(409, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.ILLEGAL_STATUS.urn, response.error.code)

        val order = shippingOrderDao.findById(103).get()
        assertEquals(ShippingOrderStatus.DELIVERED, order.status)

        verify(eventStream, never()).publish(any(), any())

        val statuses = statusDao.findByShippingOrder(order)
        assertTrue(statuses.isEmpty())
    }

    @Test
    fun cancelled() {
        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders/104/status"
        val response = rest.postForEntity(
            url,
            ChangeStatusRequest(
                status = ShippingOrderStatus.CANCELLED.name,
            ),
            Any::class.java
        )

        // THEN
        assertEquals(200, response.statusCodeValue)

        val order = shippingOrderDao.findById(104).get()
        assertEquals(ShippingOrderStatus.CANCELLED, order.status)

        verify(eventStream, never()).publish(any(), any())

        val statuses = statusDao.findByShippingOrder(order)
        assertTrue(statuses.isEmpty())
    }
}
