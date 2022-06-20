package com.wutsi.ecommerce.shipping.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.order.event.EventURN
import com.wutsi.ecommerce.order.event.OrderEventPayload
import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.service.Gateway
import com.wutsi.ecommerce.shipping.service.GatewayProvider
import com.wutsi.platform.core.logging.KVLogger
import com.wutsi.platform.core.stream.Event
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventHandler(
    private val objectMapper: ObjectMapper,
    private val gatewayProvider: GatewayProvider,
    private val dao: ShippingRepository,
    private val orderApi: WutsiOrderApi,
    private val logger: KVLogger
) {
    @EventListener
    fun onEvent(event: Event) {
        if (accept(event)) {
            // Payload
            val payload = objectMapper.readValue(event.payload, OrderEventPayload::class.java)
            logger.add("order_id", payload.orderId)

            // Order
            val order = orderApi.getOrder(payload.orderId).order
            logger.add("order_status", order.status)
            logger.add("order_shipping_id", order.shippingId)

            // Process
            val gateway = getGateway(order)
                ?: return

            logger.add("gateway", gateway::class.java.simpleName)
            when (event.type) {
                EventURN.ORDER_DONE.urn -> gateway.onOrderDone(order)
                EventURN.ORDER_OPENED.urn -> gateway.onOrderOpened(order)
                EventURN.ORDER_IN_TRANSIT.urn -> gateway.onOrderInTransit(order)
                EventURN.ORDER_DELIVERED.urn -> gateway.onOrderDelivered(order)
                else -> {}
            }
        }
    }

    private fun accept(event: Event): Boolean =
        EventURN.ORDER_DONE.urn == event.type ||
            EventURN.ORDER_OPENED.urn == event.type ||
            EventURN.ORDER_IN_TRANSIT.urn == event.type ||
            EventURN.ORDER_DELIVERED.urn == event.type

    private fun getGateway(order: Order): Gateway? {
        if (order.shippingId == null)
            return null

        val shipping = dao.findById(order.shippingId!!).get()
        return gatewayProvider.get(shipping)
    }
}
