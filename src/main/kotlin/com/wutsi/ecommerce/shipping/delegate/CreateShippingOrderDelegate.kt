package com.wutsi.ecommerce.shipping.`delegate`

import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.SetShippingOrderRequest
import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.dao.ShippingOrderStatusRepository
import com.wutsi.ecommerce.shipping.dao.ShippingRepository
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderResponse
import com.wutsi.ecommerce.shipping.entity.ShippingOrderEntity
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatusEntity
import com.wutsi.ecommerce.shipping.event.EventURN
import com.wutsi.ecommerce.shipping.event.ShippingOrderEventPayload
import com.wutsi.ecommerce.shipping.service.SecurityManager
import com.wutsi.platform.core.stream.EventStream
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class CreateShippingOrderDelegate(
    private val orderDao: ShippingOrderRepository,
    private val shippingDao: ShippingRepository,
    private val statusDao: ShippingOrderStatusRepository,
    private val orderApi: WutsiOrderApi,
    private val securityManager: SecurityManager,
    private val eventStream: EventStream,
) {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(CreateShippingOrderDelegate::class.java)
    }

    @Transactional
    public fun invoke(request: CreateShippingOrderRequest): CreateShippingOrderResponse {
        val order = orderApi.getOrder(request.orderId).order

        // Create the order
        val shippingOrder = orderDao.save(
            ShippingOrderEntity(
                orderId = order.id,
                merchantId = order.merchantId,
                customerId = order.accountId,
                status = ShippingOrderStatus.CREATED,
                shipping = shippingDao.findById(order.shippingId).get(),
                tenantId = securityManager.tenantId()
            )
        )

        // Create the status
        statusDao.save(
            ShippingOrderStatusEntity(
                shippingOrder = shippingOrder,
                status = shippingOrder.status
            )
        )

        // Set the shipping order id
        orderApi.setShippingOrder(
            request.orderId,
            SetShippingOrderRequest(
                shippingOrderId = shippingOrder.id!!
            )
        )

        // Publish event
        publish(shippingOrder, EventURN.SHIPPING_CREATED)

        return CreateShippingOrderResponse(id = shippingOrder.id)
    }

    private fun publish(shippingOrder: ShippingOrderEntity, event: EventURN) {
        try {
            eventStream.publish(event.urn, ShippingOrderEventPayload(shippingOrder.id ?: -1))
        } catch (ex: Exception) {
            LOGGER.warn("Unable to push event", ex)
        }
    }
}
