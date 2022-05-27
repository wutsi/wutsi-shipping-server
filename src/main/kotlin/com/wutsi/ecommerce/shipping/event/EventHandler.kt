package com.wutsi.ecommerce.shipping.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.ecommerce.order.event.EventURN
import com.wutsi.ecommerce.order.event.OrderEventPayload
import com.wutsi.platform.core.stream.Event
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventHandler(
    private val objectMapper: ObjectMapper,
    private val orderShippingService: OrderShippingService,
) {
    @EventListener
    fun onEvent(event: Event) {
        if (EventURN.ORDER_DONE.urn == event.type) {
            val payload = objectMapper.readValue(event.payload, OrderEventPayload::class.java)
            orderShippingService.onOrderDone(payload.orderId)
        } else if (EventURN.ORDER_CANCELLED.urn == event.type) {
            val payload = objectMapper.readValue(event.payload, OrderEventPayload::class.java)
            orderShippingService.onOrderCancelled(payload.orderId)
        }
    }
}
