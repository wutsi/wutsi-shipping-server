package com.wutsi.ecommerce.shipping.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.GetOrderResponse
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.event.OrderEventPayload
import com.wutsi.ecommerce.shipping.endpoint.AbstractSecuredController
import com.wutsi.ecommerce.shipping.service.Gateway
import com.wutsi.ecommerce.shipping.service.GatewayProvider
import com.wutsi.platform.core.stream.Event
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/EventHandler.sql"])
internal class EventHandlerTest : AbstractSecuredController() {

    @Autowired
    private lateinit var handler: EventHandler

    @MockBean
    private lateinit var orderApi: WutsiOrderApi

    @MockBean
    private lateinit var gatewayProvider: GatewayProvider

    private var gateway: Gateway = mock()

    private val order = Order(
        id = "100",
        shippingId = 100
    )

    @BeforeEach
    override fun setUp() {
        super.setUp()

        doReturn(gateway).whenever(gatewayProvider).get(any())
        doReturn(GetOrderResponse(order)).whenever(orderApi).getOrder(any())
    }

    @Test
    fun onOrderDone() {
        // GIVEN
        val payload = OrderEventPayload("111")

        // WHEN
        val event = Event(
            type = com.wutsi.ecommerce.order.event.EventURN.ORDER_DONE.urn,
            payload = ObjectMapper().writeValueAsString(payload)
        )
        handler.onEvent(event)

        // THEN
        verify(gateway).onOrderDone(order)
    }
}
