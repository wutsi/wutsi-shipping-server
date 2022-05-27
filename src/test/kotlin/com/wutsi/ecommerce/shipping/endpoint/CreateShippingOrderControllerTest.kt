package com.wutsi.ecommerce.shipping.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.GetOrderResponse
import com.wutsi.ecommerce.order.dto.Order
import com.wutsi.ecommerce.shipping.dao.ShippingOrderRepository
import com.wutsi.ecommerce.shipping.dao.ShippingOrderStatusRepository
import com.wutsi.ecommerce.shipping.dto.CreateShippingOrderRequest
import com.wutsi.ecommerce.shipping.dto.CreateShippingResponse
import com.wutsi.ecommerce.shipping.entity.ShippingOrderStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/CreateShippingOrderController.sql"])
public class CreateShippingOrderControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @MockBean
    private lateinit var orderApi: WutsiOrderApi

    @Autowired
    private lateinit var statusDao: ShippingOrderStatusRepository

    @Autowired
    private lateinit var shippingOrderDao: ShippingOrderRepository

    val order = Order(
        id = "100",
        accountId = 555,
        merchantId = 777,
        shippingId = 100L
    )

    @Test
    public fun invoke() {
        // GIVEN
        doReturn(GetOrderResponse(order)).whenever(orderApi).getOrder(any())

        // WHEN
        val url = "http://localhost:$port/v1/shipping-orders"
        val request = CreateShippingOrderRequest(orderId = "100")
        val response = rest.postForEntity(url, request, CreateShippingResponse::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val shippingOrder = shippingOrderDao.findById(response.body!!.id).get()
        assertEquals(TENANT_ID, shippingOrder.tenantId)
        assertEquals(order.merchantId, shippingOrder.merchantId)
        assertEquals(order.id, shippingOrder.orderId)
        assertEquals(ShippingOrderStatus.CREATED, shippingOrder.status)

        val status = statusDao.findByShippingOrder(shippingOrder)
        assertEquals(1, status.size)
        assertEquals(ShippingOrderStatus.CREATED, status[0].status)
        assertNull(status[0].previousStatus)
    }
}
