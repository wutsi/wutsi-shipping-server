package com.wutsi.ecommerce.shipping.event

import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.delegate.ChangeShippingOrderStatusDelegate
import com.wutsi.ecommerce.shipping.delegate.CreateShippingOrderDelegate
import com.wutsi.ecommerce.shipping.dto.ChangeStatusRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderRequest
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class OrderShippingService(
    private val create: CreateShippingOrderDelegate,
    private val status: ChangeShippingOrderStatusDelegate,
    private val shippingOrderDao: ShippingOrderRepository,
) {
    @Transactional
    fun onOrderDone(orderId: String): Long {
        val response = create.invoke(
            CreateShippingOrderRequest(orderId = orderId)
        )

        val shippingOrder = shippingOrderDao.findById(response.id).get()
        status.changeStatus(
            shippingOrder,
            ChangeStatusRequest(status = ShippingOrderStatus.READY_FOR_PICKUP.name)
        )
        return response.id
    }

    @Transactional
    fun onOrderCancelled(orderId: String) {
        val shippingOrders = shippingOrderDao.findByOrderId(orderId)
        shippingOrders.forEach {
            status.changeStatus(
                it,
                ChangeStatusRequest(
                    status = ShippingOrderStatus.CANCELLED.name,
                    reason = "order_cancelled"
                )
            )
        }
    }
}
