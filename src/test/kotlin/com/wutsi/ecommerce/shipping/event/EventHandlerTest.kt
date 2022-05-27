package com.wutsi.ecommerce.shipping.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.wutsi.ecommerce.order.event.OrderEventPayload
import com.wutsi.ecommerce.shipping.endpoint.AbstractSecuredController
import com.wutsi.platform.core.stream.Event
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class EventHandlerTest : AbstractSecuredController() {
    @MockBean
    private lateinit var order: OrderShippingService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var handler: EventHandler

    @Test
    fun onOrderDone() {
        // GIVEN
        val payload = createOrderEvent("111")

        // WHEN
        val event = Event(
            type = com.wutsi.ecommerce.order.event.EventURN.ORDER_DONE.urn,
            payload = objectMapper.writeValueAsString(payload)
        )
        handler.onEvent(event)

        // THEN
        verify(order).onOrderDone(payload.orderId)
    }

    @Test
    fun onOrderCancelled() {
        // GIVEN
        val payload = createOrderEvent("111")

        // WHEN
        val event = Event(
            type = com.wutsi.ecommerce.order.event.EventURN.ORDER_CANCELLED.urn,
            payload = objectMapper.writeValueAsString(payload)
        )
        handler.onEvent(event)

        // THEN
        verify(order).onOrderCancelled(payload.orderId)
    }

    @Test
    fun onOrderOpened() {
        // GIVEN
        val payload = createOrderEvent("111")

        // WHEN
        val event = Event(
            type = com.wutsi.ecommerce.order.event.EventURN.ORDER_OPENED.urn,
            payload = objectMapper.writeValueAsString(payload)
        )
        handler.onEvent(event)

        // THEN
        verify(order, never()).onOrderCancelled(any())
    }

    private fun createOrderEvent(id: String) = OrderEventPayload(
        orderId = id
    )
}
