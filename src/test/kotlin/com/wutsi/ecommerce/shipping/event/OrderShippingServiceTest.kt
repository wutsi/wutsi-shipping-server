package com.wutsi.ecommerce.shipping.event

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.delegate.ChangeShippingOrderStatusDelegate
import com.wutsi.ecommerce.shipping.delegate.CreateShippingOrderDelegate
import com.wutsi.ecommerce.shipping.dto.ChangeStatusRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderResponse
import com.wutsi.ecommerce.shipping.entity.ShippingOrderEntity
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import org.junit.jupiter.api.Test
import java.util.Optional
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

internal class OrderShippingServiceTest {
    private lateinit var dao: ShippingOrderRepository
    private lateinit var create: CreateShippingOrderDelegate
    private lateinit var status: ChangeShippingOrderStatusDelegate
    private lateinit var service: OrderShippingService
    private val orderId = "444"

    @BeforeTest
    fun setUp() {
        dao = mock()
        create = mock()
        status = mock()
        service = OrderShippingService(create, status, dao)
    }

    @Test
    fun onOrderDone() {
        // GIVEN
        val shippingOrder = ShippingOrderEntity(id = 555)
        doReturn(Optional.of(shippingOrder)).whenever(dao).findById(any())

        doReturn(CreateShippingOrderResponse(id = shippingOrder.id!!)).whenever(create).invoke(any())

        // WHEN
        val id = service.onOrderDone(orderId)

        // THEN
        assertEquals(shippingOrder.id, id)

        verify(create).invoke(CreateShippingOrderRequest(orderId))
        verify(status).changeStatus(
            shippingOrder,
            ChangeStatusRequest(status = ShippingOrderStatus.READY_FOR_PICKUP.name)
        )
    }

    @Test
    fun onOrderCancelled() {
        // GIVEN
        val shippingOrders = listOf(
            ShippingOrderEntity(id = 111),
            ShippingOrderEntity(id = 222),
            ShippingOrderEntity(id = 333),
        )
        doReturn(shippingOrders).whenever(dao).findByOrderId(any())

        // WHEN
        service.onOrderCancelled(orderId)

        // THEN
        verify(status, times(3)).changeStatus(any(), any())
        verify(status).changeStatus(
            shippingOrders[0],
            ChangeStatusRequest(status = ShippingOrderStatus.CANCELLED.name, reason = "order_cancelled")
        )
        verify(status).changeStatus(
            shippingOrders[1],
            ChangeStatusRequest(status = ShippingOrderStatus.CANCELLED.name, reason = "order_cancelled")
        )
        verify(status).changeStatus(
            shippingOrders[2],
            ChangeStatusRequest(status = ShippingOrderStatus.CANCELLED.name, reason = "order_cancelled")
        )
    }
}
